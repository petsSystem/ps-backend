package br.com.petshop.app.address.model.enums;

public enum Message {

    ADDRESS_ALREADY_REGISTERED("Endereço já cadastrado."),
    ADDRESS_NOT_FOUND("Endereço não cadastrado."),

    ADDRESS_ERROR_CREATE("Erro ao cadastrar novo endereço. Tente novamente mais tarde."),
    ADDRESS_ERROR_UPDATE("Erro ao atualizar dados do endereço. Tente novamente mais tarde."),
    ADDRESS_ERROR_DELETE("Erro ao excluir endereço. Tente novamente mais tarde."),
    ADDRESS_ERROR_GET("Erro ao recuperar dados do endereço. Tente novamente mais tarde."),
    ADDRESS_ERROR_PRINCIPAL("Erro ao setar endereço principal. Tente novamente mais tarde.");

    private String message;

    Message(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }
}