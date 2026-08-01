package DevBank.api_banco.dto;

import java.math.BigDecimal;

public record ComprovanteDTO(
        String mensagem,
        BigDecimal saldoAtual
) {
}