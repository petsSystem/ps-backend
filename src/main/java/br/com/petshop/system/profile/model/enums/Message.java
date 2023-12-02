package br.com.petshop.system.profile.model.enums;

public enum Message {
    PROFILE_ALREADY_REGISTERED("Perfil já cadastrado no sistema."),
    PROFILE_NOT_FOUND("Perfil não cadastrado."),
    PROFILE_ERROR_CREATE("Erro ao cadastrar perfil. Tente novamente mais tarde."),
    PROFILE_ERROR_PARTIAL("Erro ao atualizar parcialmente o perfil. Tente novamente mais tarde."),
    PROFILE_ERROR_GET("Erro ao recuperar dados do perfil. Tente novamente mais tarde."),
    PROFILE_ERROR_DELETE("Erro ao excluir dados do perfil. Tente novamente mais tarde.");

    private String message;

    Message(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }
}