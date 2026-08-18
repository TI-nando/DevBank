package DevBank.api_banco.controller;

import DevBank.api_banco.dto.*;
import DevBank.api_banco.model.Transacao;
import DevBank.api_banco.model.TransacaoRepository;
import DevBank.api_banco.model.Usuario;
import DevBank.api_banco.model.UsuarioRepository;
import DevBank.api_banco.service.OperacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController // avisa o spring que essa classe vai responder a requisições web
@RequestMapping("/usuarios") // Endereço dessa classe
public class UsuarioController {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private OperacaoService operacaoService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/teste")
    public String testeApi() {
        return "API do banco funcionando perfeitamente para o postman";
    }

    // ATUALIZADO: Agora usa DTO para não vazar a senha na resposta
    @GetMapping("/{id}")
    public ResponseEntity<DadosPerfilPublicoDTO> buscarUsuario(@PathVariable Long id) {
        Usuario usuario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não Encontrado!"));

        return ResponseEntity.ok(new DadosPerfilPublicoDTO(usuario));
    }

    @GetMapping("/{id}/extrato")
    public ResponseEntity<List<ExtratoDTO>> consultarExtrato(@PathVariable Long id) {
        // Verificador de usuarios
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build(); // Retorna erro 404 Not Found
        }

        // Passo A: Busca a lista de transações cruas do banco de dados
        List<Transacao> transacoes = transacaoRepository.buscarExtratoPorUsuario(id);

        // Passo B: Converte a Entidade (Transacao) para o nosso ExtratoDTO
        List<ExtratoDTO> extrato = transacoes.stream()
                .map(ExtratoDTO::new)
                .toList();

        // Passo C: Devolve a lista formatada com o status 200 (OK)
        return ResponseEntity.ok(extrato);
    }

    // ATUALIZADO: Recebe o DTO de cadastro (sem os campos do Spring Security) e devolve DTO limpo
    @PostMapping
    public ResponseEntity<DadosDetalhamentoUsuarioDTO> cadastrar(@RequestBody DadosCadastroUsuarioDTO dados) {
        // Pega os dados limpos do DTO e coloca em um novo Usuário
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(dados.nome());
        novoUsuario.setLogin(dados.login());
        novoUsuario.setSaldo(dados.saldo());
        novoUsuario.setLimiteCredito(new BigDecimal("500.00"));

        // Criptografar a senha antes de salvar no banco
        String senhaCriptografada = passwordEncoder.encode(dados.senha());
        novoUsuario.setSenha(senhaCriptografada);

        repository.save(novoUsuario);

        return ResponseEntity.ok(new DadosDetalhamentoUsuarioDTO(novoUsuario));
    }

    @PostMapping("/{id}/deposito")
    public ResponseEntity<ComprovanteDTO> depositar(@PathVariable Long id, @RequestBody OperacaoDTO dto) {
        BigDecimal novoSaldo = operacaoService.depositar(id, dto);

        // Monta a mensagem dinâmica com o valor
        String mensagem = "Depósito de R$ " + dto.valor() + " realizado com sucesso!";
        ComprovanteDTO comprovante = new ComprovanteDTO(mensagem, novoSaldo);

        return ResponseEntity.ok(comprovante);
    }

    @PostMapping("/{id}/saque")
    public ResponseEntity<ComprovanteDTO> sacar(@PathVariable Long id, @RequestBody OperacaoDTO dto) {
        BigDecimal novoSaldo = operacaoService.sacar(id, dto);

        // Monta a mensagem dinâmica com o valor
        String mensagem = "Saque de R$ " + dto.valor() + " realizado com sucesso!";
        ComprovanteDTO comprovante = new ComprovanteDTO(mensagem, novoSaldo);

        return ResponseEntity.ok(comprovante);
    }

    // ==========================================
    // NOVO ENDPOINT: PIX
    // ==========================================
    @PostMapping("/{id}/pix")
    public ResponseEntity<ComprovanteDTO> fazerPix(@PathVariable Long id, @RequestBody PixDTO pixDTO) {
        // O OperacaoService faz a transferência e nos devolve o saldo atualizado
        BigDecimal novoSaldo = operacaoService.realizarPix(id, pixDTO);

        // Monta a mensagem dinâmica para o comprovante
        String mensagem = "Pix de R$ " + pixDTO.valor() + " enviado com sucesso para a chave: " + pixDTO.chaveDestino();
        ComprovanteDTO comprovante = new ComprovanteDTO(mensagem, novoSaldo);

        return ResponseEntity.ok(comprovante);
    }
}