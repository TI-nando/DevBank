package DevBank.api_banco.controller;

import DevBank.api_banco.dto.ExtratoDTO;
import DevBank.api_banco.dto.OperacaoDTO;
import DevBank.api_banco.model.Transacao;
import DevBank.api_banco.model.TransacaoRepository;
import DevBank.api_banco.model.Usuario;
import DevBank.api_banco.model.UsuarioRepository;
import DevBank.api_banco.service.OperacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // aviasa o spring que essa classe vai responder a requisições web
@RequestMapping("/usuarios") // Endereço dessa classee
public class UsuarioController {

    @Autowired // O spring faz a Injeção automatica das dependencias.
    private UsuarioRepository repository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private OperacaoService operacaoService;

    @GetMapping("/teste")
    public String testeApi() {
        return "API do banco funcionando perfeitamente para o postman";
    }

    @PostMapping
    public Usuario cadastrar(@RequestBody Usuario novoUsuario) {
        // @RequestBody fala para o spring pegar o JSON do POSTMAN e transformar em um objeto Usuario.
        return  repository.save(novoUsuario);
    }

    // {id} indica que sera recebido valor dinamico da URL.
    @GetMapping("/{id}")
    public Usuario buscarUsuario(@PathVariable Long id) {
        // vai no banco, buca o ID, caso nao encontre, responde com um ERRO.
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

    @PostMapping("/{id}/deposito")
    public ResponseEntity<String> depositar(@PathVariable Long id, @RequestBody OperacaoDTO dto) {
        operacaoService.depositar(id, dto);
        return ResponseEntity.ok("Depósito realizado com sucesso!");
    }

    @PostMapping("/{id}/saque")
    public ResponseEntity<String> sacar(@PathVariable Long id, @RequestBody OperacaoDTO dto) {
        operacaoService.sacar(id, dto);
        return ResponseEntity.ok("Saque realizado com sucesso!");
    }
}
