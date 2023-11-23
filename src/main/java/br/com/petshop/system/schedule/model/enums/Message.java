package br.com.petshop.system.schedule.model.enums;

public enum Message {
    EMPLOYEE_ALREADY_REGISTERED("Funcionário já cadastrado no sistema."),
    EMPLOYEE_NOT_FOUND("Cadastro do funcionário não encontrado."),
    EMPLOYEE_ERROR_PARTIAL("Erro ao atualizar parcialmente os dados do funcionário. Tente novamente mais tarde."),
    EMPLOYEE_ERROR_CREATE("Erro ao cadastrar funcionário. Tente novamente mais tarde."),
    EMPLOYEE_ERROR_CREATE_COMPANY("Empresa / Loja não existe."),
    EMPLOYEE_ERROR_UPDATE("Erro ao atualizar dados do funcionário. Tente novamente mais tarde."),
    EMPLOYEE_ERROR_GET("Erro ao recuperar dados do funcionário. Tente novamente mais tarde."),
    EMPLOYEE_ERROR_COMPANY_ID_SELECT("Funcionário com mais de uma empresa associada. Necessário selecionar uma empresa/loja."),
    EMPLOYEE_ERROR_COMPANY_ID_FORBIDDEN("Funcionário não pertence a empresa/loja informada."),
    EMPLOYEE_ERROR_FORBIDDEN("Acesso negado."),
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