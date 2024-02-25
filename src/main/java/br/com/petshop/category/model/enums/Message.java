package br.com.petshop.category.model.enums;

/**
 * Enum das mensagens de retorno de erros
 */
public enum Message {
    CATEGORY_CREATE_ERROR ("Erro ao cadastrar categoria. Tente novamente mais tarde."),
    CATEGORY_ACTIVATE_ERROR ("Erro ao ativar/desativar categoria. Tente novamente mais tarde."),
    CATEGORY_NOT_FOUND_ERROR ("Categoria não encontrada."),
    CATEGORY_FORBIDDEN_ERROR ("Acesso negado."),
    CATEGORY_UPDATE_ERROR ("Erro ao atualizar categoria. Tente novamente mais tarde."),
    CATEGORY_GET_ERROR ("Erro ao recuperar dados da(s) categoria(s). Tente novamente mais tarde.");

    private String message;

    Message(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }
}