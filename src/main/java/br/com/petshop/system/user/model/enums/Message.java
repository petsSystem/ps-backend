package br.com.petshop.system.user.model.enums;

public enum Message {
    USER_ALREADY_REGISTERED("Usuário já cadastrado no sistema."),
    USER_NOT_FOUND("Usuário não cadastrado."),
    USER_EMPLOYEE_NOT_FOUND("Funcionário não existe."),
    USER_ERROR_FORBIDDEN("Acesso proibido."),
    USER_ERROR_CREATE("Erro ao cadastrar usuário. Tente novamente mais tarde."),
    USER_ERROR_SENDING_PASSWORD("Erro ao enviar email com nova senha. Tente novamente mais tarde."),

    USER_ERROR_PARTIAL("Erro ao atualizar parcialmente os dados do funcionário. Tente novamente mais tarde."),

    EMPLOYEE_ERROR_CREATE_COMPANY("Empresa / Loja não existe."),
    EMPLOYEE_ERROR_UPDATE("Erro ao atualizar dados do funcionário. Tente novamente mais tarde."),
    EMPLOYEE_ERROR_GET("Erro ao recuperar dados do funcionário. Tente novamente mais tarde."),
    EMPLOYEE_ERROR_COMPANY_ID_SELECT("Funcionário com mais de uma empresa associada. Necessário selecionar uma empresa/loja."),
    EMPLOYEE_ERROR_COMPANY_ID_FORBIDDEN("Funcionário não pertence a empresa/loja informada."),
    EMPLOYEE_ERROR_DEACTIVATE("Erro ao desativar dados do funcionário. Tente novamente mais tarde."),
    EMPLOYEE_ERROR_ACTIVATE("Erro ao ativar dados do funcionário. Tente novamente mais tarde."),
    EMPLOYEE_ERROR_DELETE("Erro ao excluir dados do funcionário. Tente novamente mais tarde.");

    private String message;

    Message(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }
}