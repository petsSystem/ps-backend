package br.com.petshop.profile.model.enums;

/**
 * Enum das mensagens de retorno de erros
 */
public enum Message {
    PROFILE_CREATE_ERROR("Erro ao cadastrar perfil. Tente novamente mais tarde."),
    PROFILE_ALREADY_REGISTERED_ERROR("Perfil já cadastrado."),
    PROFILE_NOT_FOUND_ERROR("Perfil não encontrado."),
    PROFILE_UPDATE_ERROR("Erro ao atualizar o perfil. Tente novamente mais tarde."),
    PROFILE_GET_ERROR("Erro ao recuperar dados do perfil. Tente novamente mais tarde.");

    private String message;

    Message(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }
}