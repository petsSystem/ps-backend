package br.com.petshop.pet.model.enums;

/**
 * Enum das mensagens de retorno de erros
 */
public enum Message {
    PET_LIST_ERROR("Erro ao recuperar dados do pet. Tente novamente mais tarde."),
    PET_CREATE_ERROR("Erro ao cadastrar pet. Tente novamente mais tarde."),
    PET_NOT_FOUND_ERROR("Pet não encontrado."),
    PET_UPDATE_ERROR("Erro ao atualizar dados do pet. Tente novamente mais tarde."),
    PET_GET_ERROR("Erro ao recuperar dados do(s) pet(s). Tente novamente mais tarde."),
    PET_DELETE_ERROR("Erro ao excluir pet. Tente novamente mais tarde."),

    ;

    private String message;

    Message(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }
}