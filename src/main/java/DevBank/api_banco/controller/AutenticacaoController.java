package DevBank.api_banco.controller;

import DevBank.api_banco.dto.DadosAutenticacaoDTO;
import DevBank.api_banco.dto.DadosTokenJWT;
import DevBank.api_banco.infra.TokenService;
import DevBank.api_banco.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody DadosAutenticacaoDTO dados) {

        // 1. Cria um token temporário só com login e senha para o Spring validar
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());

        // 2. O Spring vai no banco (através daquele Service que criamos) e checa se a senha criptografada bate
        var authentication = manager.authenticate(authenticationToken);

        // 3. Se a senha bater, nós chamamos nossa "fábrica de crachás" para gerar o JWT
        var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

        // 4. Devolvemos o token pronto para o usuário!
        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }
}
