package DevBank.api_banco.dto;

import DevBank.api_banco.model.Transacao;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExtratoDTO(
        Long idTransacao,
        Long idRemetente,
        Long idDestinatario,
        BigDecimal valor,
        LocalDateTime dataHora
) {
    public ExtratoDTO(Transacao transacao) {
        this(
                transacao.getId(),
                transacao.getRemetente().getId(),
                transacao.getDestinatario().getId(),
                transacao.getValor(),
                transacao.getDataHora()
        );
    }
}