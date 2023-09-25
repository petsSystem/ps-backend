package br.com.petshop.app.pet.model.enums;

public enum Message {
    PET_ALREADY_REGISTERED("Pet já cadastrado no sistema."),
    PET_NOT_FOUND("Cadastro do pet não encontrado."),

    PET_ERROR_CREATE("Erro ao cadastrar pet. Tente novamente mais tarde."),
    PET_ERROR_UPDATE("Erro ao atualizar pet. Tente novamente mais tarde."),
    PET_ERROR_DELETE("Erro ao excluir dados do pet. Tente novamente mais tarde."),
    PET_ERROR_GET("Erro ao recuperar dados do pet. Tente novamente mais tarde.");

    private String message;

    Message(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }
}