package br.com.petshop.customer.model.enums;

public enum Message {

    CUSTOMER_ALREADY_REGISTERED_ERROR ("Usuário já cadastrado no sistema."),
    CUSTOMER_CREATE_ERROR ("Erro ao cadastrar usuário. Tente novamente mais tarde."),
    CUSTOMER_NOT_FOUND_ERROR ("Usuário não encontrado."),
    CUSTOMER_OLD_PASSWORD_ERROR ("Senha atual informada está incorreta."),
    CUSTOMER_CHANGING_PASSWORD_ERROR ("Erro ao atualizar nova senha. Tente novamente mais tarde."),
    USER_NOT_FOUND("Usuário/Email não cadastrado."),
    USER_COMPANY_NOT_FOUND("Não há petshop(s) próximo(s) ao endereço informado."),
    USER_ERROR_INVALID_TOKEN("Token inválido."),
    USER_ERROR_VALIDATE_EMAIL("Erro ao validar email. Tente novamente mais tarde."),
    USER_ERROR_EXPIRED_TOKEN("Token expirado. Solicite novo token."),
    USER_ERROR_RESEND_TOKEN("Erro ao reenviar email de validação. Tente novamente mais tarde."),
    USER_ERROR_SEND_EMAIL("Erro ao enviar email. Tente novamente mais tarde."),
    USER_ERROR_CHANGE_PASSWORD("Erro ao trocar senha. Tente novamente mais tarde."),
    USER_ERROR_CREATE("Erro ao cadastrar usuário. Tente novamente mais tarde."),
    USER_ERROR_UPDATE("Erro ao atualizar dados do usuário. Tente novamente mais tarde."),
    USER_ERROR_DELETE("Erro ao excluir dados do usuário. Tente novamente mais tarde."),
    USER_ERROR_GET("Erro ao retornar dados do usuário. Tente novamente mais tarde.");

    private String message;

    Message(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }
}