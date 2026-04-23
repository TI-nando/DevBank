package DevBank.api_banco.model.dto;

import java.math.BigDecimal;

public record TransferenciaDTO(Long idRemetente, Long idDestinatario, BigDecimal valor) {

}
