package DevBank.api_banco.dto;

import java.math.BigDecimal;

public record TransferenciaDTO(Long idRemetente, Long idDestinatario, BigDecimal valor) {

}
