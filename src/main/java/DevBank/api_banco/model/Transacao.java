package DevBank.api_banco.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name = "Transacao")
@Table(name = "transacoes")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ----- Remetente -----
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "remetente_id")
    private Usuario remetente;

    // ----- Destinatario -----
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinatario_id")
    private Usuario destinatario;

    private BigDecimal valor;

    // ----- Data e hora da transação -----
    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    public Transacao(Usuario remetente, Usuario destinatario, BigDecimal valor) {
        this.remetente = remetente;
        this.destinatario = destinatario;
        this.valor = valor;
        this.dataHora = LocalDateTime.now(); // ----- Pega a hora local do servidor -----
    }
}
