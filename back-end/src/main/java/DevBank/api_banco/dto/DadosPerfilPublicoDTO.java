package DevBank.api_banco.dto;

import DevBank.api_banco.model.Usuario;

public record DadosPerfilPublicoDTO(String nome, String login) {

    //Já converte a Entidade no DTO público
    public DadosPerfilPublicoDTO(Usuario usuario) {
        this(usuario.getNome(), usuario.getLogin());
    }
}