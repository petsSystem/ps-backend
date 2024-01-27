package br.com.petshop.customer.model.enums;

public enum Message {
    CUSTOMER_ALREADY_REGISTERED_ERROR ("Usuário já cadastrado no sistema."),
    CUSTOMER_CREATE_ERROR ("Erro ao cadastrar usuário. Tente novamente mais tarde."),
    CUSTOMER_NOT_FOUND_ERROR ("Usuário não encontrado."),
    CUSTOMER_OLD_PASSWORD_ERROR ("Senha atual informada está incorreta."),
    CUSTOMER_CHANGING_PASSWORD_ERROR ("Erro ao atualizar nova senha. Tente novamente mais tarde."),
    CUSTOMER_UPDATE_ERROR("Erro ao atualizar dados do usuário. Tente novamente mais tarde."),
    CUSTOMER_UNAUTHORIZED_ERROR("Token inválido."),
    CUSTOMER_VALIDATE_EMAIL_ERROR("Erro ao validar email. Tente novamente mais tarde."),
    CUSTOMER_RESEND_TOKEN_ERROR("Erro ao reenviar email de validação. Tente novamente mais tarde."),
    CUSTOMER_DELETE_ERROR("Erro ao excluir dados do usuário. Tente novamente mais tarde."),
    CUSTOMER_SEND_EMAIL_ERROR("Erro ao enviar email. Tente novamente mais tarde."),
    CUSTOMER_GET_ERROR("Erro ao retornar dados do usuário. Tente novamente mais tarde.");

    private String message;

    Message(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }
}