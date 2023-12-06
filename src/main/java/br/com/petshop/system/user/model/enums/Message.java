package br.com.petshop.system.user.model.enums;

public enum Message {
    USER_ALREADY_REGISTERED("Usuário já cadastrado no sistema."),
    USER_NOT_FOUND("Usuário não cadastrado."),
    USER_EMPLOYEE_NOT_FOUND("Cadastro de funcionário não existe."),
    USER_ERROR_FORBIDDEN("Acesso negado."),
    USER_ERROR_CREATE("Erro ao cadastrar usuário. Tente novamente mais tarde."),
    USER_ERROR_SENDING_PASSWORD("Erro ao enviar email com nova senha. Tente novamente mais tarde."),
    USER_ERROR_PARTIAL("Erro ao atualizar parcialmente os dados do usuário. Tente novamente mais tarde."),
    USER_ERROR_GET("Erro ao recuperar dados do usuário. Tente novamente mais tarde."),
    USER_ERROR_DELETE("Erro ao excluir dados do usuário. Tente novamente mais tarde.");

    private String message;

    Message(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }
}