package br.com.petshop.product.model.enums;

/**
 * Enum das mensagens de retorno de erros
 */
public enum Message {
    PRODUCT_CREATE_ERROR ("Erro ao cadastrar produto/serviço. Tente novamente mais tarde."),
    PRODUCT_FORBIDDEN_ERROR ("Acesso negado."),
    PRODUCT_ALREADY_REGISTERED_ERROR ("Produto/serviço já cadastrado."),
    PRODUCT_ACTIVATE_ERROR ("Erro ao ativar/desativar produto/serviço. Tente novamente mais tarde."),
    PRODUCT_NOT_FOUND_ERROR ("Produto/serviço não encontrado."),
    PRODUCT_UPDATE_ERROR ("Erro ao atualizar produto/serviço. Tente novamente mais tarde."),
    PRODUCT_GET_ERROR ("Erro ao recuperar dados do(s) produto(s)/serviço(s). Tente novamente mais tarde.");

    private String message;

    Message(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }
}