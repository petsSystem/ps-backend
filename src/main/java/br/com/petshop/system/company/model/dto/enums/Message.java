package br.com.petshop.system.company.model.dto.enums;

public enum Message {
    COMPANY_ALREADY_REGISTERED("Empresa já cadastrada no sistema."),
    COMPANY_NOT_FOUND("Cadastro da empresa não encontrado."),

    COMPANY_ERROR_CREATE("Erro ao cadastrar empresa. Tente novamente mais tarde."),
    COMPANY_ERROR_UPDATE("Erro ao atualizar dados da empresa. Tente novamente mais tarde."),
    COMPANY_ERROR_GET("Erro ao recuperar dados da empresa. Tente novamente mais tarde."),
    COMPANY_ERROR_DEACTIVATE("Erro ao desativar empresa. Tente novamente mais tarde."),
    COMPANY_ERROR_ACTIVATE("Erro ao ativar empresa. Tente novamente mais tarde."),
    COMPANY_ERROR_DELETE("Erro ao excluir empresa. Tente novamente mais tarde."),;

    private String message;

    Message(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }
}