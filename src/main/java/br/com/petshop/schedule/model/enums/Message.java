package br.com.petshop.schedule.model.enums;

/**
 * Enum das mensagens de retorno de erros
 */
public enum Message {
    SCHEDULE_CREATE_ERROR ("Erro ao cadastrar agenda. Tente novamente mais tarde."),
    SCHEDULE_ALREADY_REGISTERED_ERROR ("Agenda já cadastrada."),
    SCHEDULE_USER_NOT_FOUND_ERROR ("Usuário não encontrado."),
    SCHEDULE_ACTIVATE_ERROR ("Erro ao ativar/desativar agenda. Tente novamente mais tarde."),
    SCHEDULE_NOT_FOUND_ERROR ("Agenda não encontrada."),
    SCHEDULE_UPDATE_ERROR ("Erro ao atualizar agenda. Tente novamente mais tarde."),
    SCHEDULE_GET_ERROR ("Erro ao recuperar dados da(s) agenda(s). Tente novamente mais tarde.");

    private String message;

    Message(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }
}