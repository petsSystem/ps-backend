package br.com.petshop.app.user.model.enums;

public enum Message {

    //USER MESSAGES
    USER_ALREADY_REGISTERED("Usuário/Email já cadastrado."),
    USER_NOT_FOUND("Usuário/Email não cadastrado."),
    USER_ERROR_INVALID_TOKEN("Token inválido."),
    USER_ERROR_VALIDATE_EMAIL("Erro ao validar email. Tente novamente mais tarde."),
    USER_ERROR_EXPIRED_TOKEN("Token expirado. Solicite novo token."),
    USER_ERROR_RESEND_TOKEN("Erro ao reenviar email de validação. Tente novamente mais tarde."),
    USER_ERROR_SEND_EMAIL("Erro ao enviar email. Tente novamente mais tarde."),
    USER_ERROR_CHANGE_PASSWORD("Erro ao trocar senha. Tente novamente mais tarde."),
    USER_ERROR_CREATE("Erro ao cadastrar usuário. Tente novamente mais tarde."),
    USER_ERROR_UPDATE("Erro ao atualizar dados do usuário. Tente novamente mais tarde."),
    USER_ERROR_DELETE("Erro ao excluir dados do usuário. Tente novamente mais tarde."),
    USER_ERROR_GET("Erro ao retornar dados do usuário. Tente novamente mais tarde."),

    //ADDRESS MESSAGES
    ADDRESS_ALREADY_REGISTERED("Endereço já cadastrado."),
    ADDRESS_NOT_FOUND("Endereço não cadastrado."),

    ADDRESS_ERROR_CREATE("Erro ao cadastrar novo endereço. Tente novamente mais tarde."),
    ADDRESS_ERROR_UPDATE("Erro ao atualizar dados do endereço. Tente novamente mais tarde."),
    ADDRESS_ERROR_DELETE("Erro ao excluir endereço. Tente novamente mais tarde."),
    ADDRESS_ERROR_GET("Erro ao recuperar dados do endereço. Tente novamente mais tarde.");

    private String message;

    Message(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }
}