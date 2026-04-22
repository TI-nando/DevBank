package DevBank.api_banco.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity // mapeia essa classe para o banco de dados.
@Table(name = "usuarios")
@Getter
@Setter

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO INCREMENTA
    private Long id;
    private String nome;
    private String login;
    private String senha;
    private BigDecimal saldo;
}
