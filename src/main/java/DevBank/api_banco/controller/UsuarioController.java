package DevBank.api_banco.controller;

import DevBank.api_banco.dto.ComprovanteDTO;
import DevBank.api_banco.dto.ExtratoDTO;
import DevBank.api_banco.dto.OperacaoDTO;
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


    // {id} indica que sera recebido valor dinamico da URL.
    @GetMapping("/{id}")
    public Usuario buscarUsuario(@PathVariable Long id) {
        // vai no banco, busca o ID, caso nao encontre, responde com um ERRO.
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não Encontrado!"));
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

    @PostMapping
    public Usuario cadastrar(@RequestBody Usuario novoUsuario) {
        // Criptografar a senha antes de salvar no banco
        String senhaCriptografada = passwordEncoder.encode(novoUsuario.getSenha());
        novoUsuario.setSenha(senhaCriptografada);

        return repository.save(novoUsuario);
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
}