package DevBank.api_banco.dto;

import java.math.BigDecimal;

public record DadosCadastroUsuarioDTO(
        String nome,
        String login,
        String senha,
        BigDecimal saldo,
        String chavePix
) {
}
