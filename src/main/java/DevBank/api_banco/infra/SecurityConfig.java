package DevBank.api_banco.infra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // Desabilita proteção contra CSRF.
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // API não guarda sessão (estado)
                .authorizeHttpRequests(req -> {
                    // Portões Abertos (Públicos)
                    req.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll(); // Permissão para o swagger
                    req.requestMatchers(HttpMethod.POST, "/usuarios").permitAll(); // Libera o cadastro de novos usuários

                    // Portões Fechados (Exigem Token)
                    req.anyRequest().authenticated(); // Qualquer outra rota exige autenticação
                })
                .build();
    }

    // Usar a criptografia BCrypt para senhas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
