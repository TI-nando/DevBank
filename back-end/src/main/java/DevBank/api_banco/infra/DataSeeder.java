package DevBank.api_banco.infra;

import DevBank.api_banco.model.Usuario;
import DevBank.api_banco.model.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

@Configuration
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Só cria se o banco estiver vazio
        if (repository.count() == 0) {

            Usuario user1 = new Usuario();
            user1.setNome("Fernando Henrique");
            user1.setLogin("fernando");
            user1.setSenha(passwordEncoder.encode("123456"));
            user1.setSaldo(new BigDecimal("1000.00"));

            Usuario user2 = new Usuario();
            user2.setNome("Pedro Miguel");
            user2.setLogin("pedro");
            user2.setSenha(passwordEncoder.encode("123456"));
            user2.setSaldo(new BigDecimal("10000.00"));

            repository.save(user1);
            repository.save(user2);

            System.out.println("✅ Semeador executado: Usuarios Fernando e Pedro criados com sucesso!");
        }
    }
}
