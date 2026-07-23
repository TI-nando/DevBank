package DevBank.api_banco.controller;

import DevBank.api_banco.dto.ExtratoDTO;
import DevBank.api_banco.model.Transacao;
import DevBank.api_banco.model.TransacaoRepository;
import DevBank.api_banco.model.Usuario;
import DevBank.api_banco.model.UsuarioRepository;
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

        // Passo A: Busca a lista de transações cruas do banco de dados
        List<Transacao> transacoes = transacaoRepository.buscarExtratoPorUsuario(id);

        // Passo B: Converte a Entidade (Transacao) para o nosso ExtratoDTO
        List<ExtratoDTO> extrato = transacoes.stream()
                .map(ExtratoDTO::new)
                .toList();

        // Passo C: Devolve a lista formatada com o status 200 (OK)
        return ResponseEntity.ok(extrato);
    }
}
