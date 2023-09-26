package br.com.petshop.system.subsidiary.model.enums;

public enum Message {
    SUBSIDIARY_ALREADY_REGISTERED("Estabelecimento já cadastrado no sistema."),
    SUBSIDIARY_NOT_FOUND("Cadastro do estabelecimento não encontrado."),

    SUBSIDIARY_ERROR_CREATE("Erro ao cadastrar estabelecimento. Tente novamente mais tarde."),
    SUBSIDIARY_ERROR_UPDATE("Erro ao atualizar dados do estabelecimento. Tente novamente mais tarde."),
    SUBSIDIARY_ERROR_GET("Erro ao recuperar dados do estabelecimento. Tente novamente mais tarde."),
    SUBSIDIARY_ERROR_DEACTIVATE("Erro ao desativar estabelecimento. Tente novamente mais tarde."),
    SUBSIDIARY_ERROR_ACTIVATE("Erro ao ativar estabelecimento. Tente novamente mais tarde."),
    SUBSIDIARY_ERROR_DELETE("Erro ao excluir estabelecimento. Tente novamente mais tarde."),;

    private String message;

    Message(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }
}