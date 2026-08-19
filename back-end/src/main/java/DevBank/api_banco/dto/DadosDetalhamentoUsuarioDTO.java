package DevBank.api_banco.dto;

import DevBank.api_banco.model.Usuario;

import java.math.BigDecimal;

public record DadosDetalhamentoUsuarioDTO(Long id, String nome, String login, BigDecimal saldo) {

    public DadosDetalhamentoUsuarioDTO(Usuario usuario) {
        this(usuario.getId(), usuario.getNome(), usuario.getLogin(), usuario.getSaldo());
    }
}
