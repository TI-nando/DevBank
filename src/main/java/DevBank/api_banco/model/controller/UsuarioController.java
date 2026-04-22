package DevBank.api_banco.model.controller;

import DevBank.api_banco.model.Usuario;
import DevBank.api_banco.model.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController // aviasa o spring que essa classe vai responder a requisições web
@RequestMapping("/usuarios") // Endereço dessa classee
public class UsuarioController {

    @Autowired // O spring faz a Injeção automatica das dependencias.
    private UsuarioRepository repository;

    @GetMapping("/teste")
    public String testeApi() {
        return "API do banco funcionando perfeitamente para o postman";
    }

    @PostMapping
    public Usuario cadastrar(@RequestBody Usuario novoUsuario) {
        // @RequestBody fala para o spring pegar o JSON do POSTMAN e transformar em um objeto Usuario.
        return  repository.save(novoUsuario);
    }
}
