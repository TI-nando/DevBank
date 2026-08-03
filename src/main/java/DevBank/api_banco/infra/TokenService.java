package DevBank.api_banco.infra;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import DevBank.api_banco.model.Usuario;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // Senha secreta (ALTERAR QUANDO FOR PARA PRODUÇÃO)
    private final String secret = "teste";

    public String gerarToken(Usuario usuario) {
        try {
            // O algoritmo HMAC256 vai embaralhar os dados
            Algorithm algoritmo = Algorithm.HMAC256(secret);

            return JWT.create()
                    .withIssuer("DevBank API") // Cria o Token
                    .withSubject(usuario.getLogin()) // Dono do token
                    .withExpiresAt(dataExpiracao()) // Validade
                    .sign(algoritmo); // Assina e finaliza

        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token jwt", exception);
        }
    }

    // O Token só dura 2 horas. Depois disso o usuário tem que logar de novo.
    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
