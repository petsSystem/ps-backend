package br.com.petshop.company.model.enums;

/**
 * Enum das mensagens de retorno de erros
 */
public enum Message {
    COMPANY_CREATE_ERROR ("Erro ao cadastrar loja. Tente novamente mais tarde."),
    COMPANY_ALREADY_REGISTERED_ERROR ("Loja já cadastrada."),
    COMPANY_ACTIVATE_ERROR ("Erro ao ativar/desativar loja. Tente novamente mais tarde."),
    COMPANY_NOT_FOUND_ERROR ("Loja não encontrada ou inativa."),
    COMPANY_UPDATE_ERROR ("Erro ao atualizar loja. Tente novamente mais tarde."),
    COMPANY_GET_ERROR ("Erro ao recuperar dados da(s) loja(s). Tente novamente mais tarde."),
    COMPANY_FORBIDDEN_ERROR ("Acesso negado."),
    COMPANY_GEO_ERROR("Não foi possível buscar estabelecimentos próximos ao endereço informado. Tente novamente mais tarde"),;

    private String message;

    Message(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }
}