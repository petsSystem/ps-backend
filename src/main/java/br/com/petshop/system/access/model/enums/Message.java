package br.com.petshop.system.access.model.enums;

public enum Message {
    ACCESS_GROUP_ALREADY_REGISTERED("Grupo de acesso já cadastrado no sistema."),
    ACCESS_GROUP_NOT_FOUND("Grupo de acesso não cadastrado."),
    ACCESS_GROUP_ERROR_CREATE("Erro ao cadastrar grupo de acesso. Tente novamente mais tarde."),
    ACCESS_GROUP_ERROR_PARTIAL("Erro ao atualizar parcialmente o grupo de acesso. Tente novamente mais tarde."),
    ACCESS_GROUP_ERROR_GET("Erro ao recuperar dados do grupo de acesso. Tente novamente mais tarde."),
    ACCESS_GROUP_ERROR_DELETE("Erro ao excluir dados do grupo de acesso. Tente novamente mais tarde.");

    private String message;

    Message(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }
}