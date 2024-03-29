package br.com.petshop.user.model.enums;

/**
 * Enum das mensagens de retorno de erros
 */
public enum Message {
    USER_CREATE_ERROR ("Erro ao cadastrar usuário. Tente novamente mais tarde."),
    USER_COMPANY_NOT_ACTIVE_ERROR ("Loja cadastrada está inativa."),
    USER_COMPANY_NOT_FOUND_ERROR ("Loja não encontrada."),
    USER_PROFILE_PARAM_MISSING_ERROR ("Favor informar o perfil do usuário."),
    USER_ALREADY_REGISTERED_ERROR ("Usuário já cadastrado no sistema."),
    USER_ACTIVATE_ERROR ("Erro ao ativar/desativar usuário. Tente novamente mais tarde."),
    USER_NOT_FOUND_ERROR ("Usuário não encontrado."),
    USER_OLD_PASSWORD_ERROR ("Senha atual informada está incorreta."),
    USER_CHANGING_PASSWORD_ERROR ("Erro ao atualizar nova senha. Tente novamente mais tarde."),
    USER_SENDING_PASSWORD_ERROR ("Erro ao enviar email com nova senha. Tente novamente mais tarde."),
    USER_FORBIDDEN_ERROR("Acesso negado."),
    USER_UPDATE_ERROR("Erro ao atualizar dados do usuário. Tente novamente mais tarde."),
    USER_GET_ERROR("Erro ao recuperar dados do usuário. Tente novamente mais tarde.");

    private String message;

    Message(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }
}