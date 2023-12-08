package br.com.petshop.system.company.model.enums;

public enum Message {
    COMPANY_CREATE_ERROR("Erro ao cadastrar loja. Tente novamente mais tarde."),
    COMPANY_ALREADY_REGISTERED_ERROR("Loja já cadastrada no sistema."),
    COMPANY_ACTIVATE_ERROR("Erro ao ativar/desativar loja. Tente novamente mais tarde."),
    COMPANY_NOT_FOUND_ERROR("Loja não encontrada."),
    COMPANY_UPDATE_ERROR("Erro ao atualizar loja. Tente novamente mais tarde."),
    COMPANY_NOT_ACTIVE_ERROR("Loja inativa."),
    COMPANY_GET_ERROR("Erro ao recuperar dados da(s) loja(s). Tente novamente mais tarde."),
    COMPANY_FORBIDDEN_ERROR("Acesso negado."),



    COMPANY_PARTIAL_ERROR("Erro ao atualizar parcialmente os dados da empresa. Tente novamente mais tarde.");

    private String message;

    Message(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }
}