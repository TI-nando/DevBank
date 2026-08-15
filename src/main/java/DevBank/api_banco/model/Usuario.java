package DevBank.api_banco.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

@Entity // mapeia essa classe para o banco de dados.
@Table(name = "usuarios")
@Getter
@Setter
public class Usuario implements UserDetails { // <-- Assinando o contrato do Spring Security

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO INCREMENTA
    @Column(unique = true)
    private Long id;
    private String nome;
    private String login;
    private String senha;
    private BigDecimal saldo;
    private String chavePix;

    // MÉTODOS OBRIGATÓRIOS DO CONTRATO USER DETAILS (SPRING SECURITY)

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Dizemos ao Spring que todo mundo que se cadastrar tem o perfil de usuário comum
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return this.senha; // Retorna ao Spring: O Password é a nossa "senha"
    }

    @Override
    public String getUsername() {
        return this.login; // Retorna ao Spring: O Username é o nosso "login"
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Conta não expira
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Conta não está bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Senha não expira
    }

    @Override
    public boolean isEnabled() {
        return true; // Usuário está ativo
    }
}