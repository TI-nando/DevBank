package DevBank.api_banco.infra;

import DevBank.api_banco.model.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Diz ao Spring que ele precisa gerenciar essa classe
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Tenta pegar o token do cabeçalho da requisição
        var tokenJWT = recuperarToken(request);

        // 2. Se mandou um token...
        if (tokenJWT != null) {

            // 3. Lê o token e descobre quem é
            var subject = tokenService.getSubject(tokenJWT);

            // 4. Vai no banco e pega os dados completos desse usuário
            var usuario = repository.findByLogin(subject);

            // 5. Força o login no Spring Security (diz que ele está autenticado na requisição atual)
            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 6. Continua o fluxo normal da requisição (vai pro Controller de saque/depósito)
        filterChain.doFilter(request, response);
    }

    // Método auxiliar para limpar a palavra "Bearer " e pegar só o código do Token

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}