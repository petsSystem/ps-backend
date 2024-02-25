package br.com.petshop.customer.model.enums;

/**
 * Enum das mensagens de retorno de erros
 */
public enum Message {
    CUSTOMER_ALREADY_REGISTERED_ERROR ("Cliente já cadastrado no sistema."),
    CUSTOMER_CREATE_ERROR ("Erro ao cadastrar cliente. Tente novamente mais tarde."),
    CUSTOMER_NOT_FOUND_ERROR ("Cliente não encontrado."),
    CUSTOMER_OLD_PASSWORD_ERROR ("Senha atual informada está incorreta."),
    CUSTOMER_CHANGING_PASSWORD_ERROR ("Erro ao atualizar nova senha. Tente novamente mais tarde."),
    CUSTOMER_UPDATE_ERROR("Erro ao atualizar dados do cliente. Tente novamente mais tarde."),
    CUSTOMER_UNAUTHORIZED_ERROR("Token inválido."),
    CUSTOMER_VALIDATE_EMAIL_ERROR("Erro ao validar email. Tente novamente mais tarde."),
    CUSTOMER_RESEND_TOKEN_ERROR("Erro ao reenviar email para validação. Tente novamente mais tarde."),
    CUSTOMER_DELETE_ERROR("Erro ao cancelar conta. Tente novamente mais tarde."),
    CUSTOMER_SEND_EMAIL_ERROR("Erro ao enviar email. Tente novamente mais tarde."),
    CUSTOMER_GET_ERROR("Erro ao retornar dados do(s) cliente(s). Tente novamente mais tarde."),
    CUSTOMER_ASSOCIATE_ERROR("Erro ao associar cliente a loja. Tente novamente mais tarde"),
    CUSTOMER_FAVORITE_ERROR("Erro ao favoritar petshop. Tente novamente mais tarde."),
    CUSTOMER_UNFAVORITE_ERROR("Erro ao desassociar petshop de cliente. Tente novamente mais tarde.");

    private String message;

    Message(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }
}