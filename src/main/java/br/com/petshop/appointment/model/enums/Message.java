package br.com.petshop.appointment.model.enums;

/**
 * Enum das mensagens de retorno de erros
 */
public enum Message {
    APPOINTMENT_CREATE_ERROR ("Erro ao cadastrar agendamento. Tente novamente mais tarde."),
    APPOINTMENT_NOT_FOUND_ERROR ("Agendamento não encontrado"),
    APPOINTMENT_UPDATE_ERROR ("Erro ao atualizar agendamento. Tente novamente mais tarde."),
    APPOINTMENT_STATUS_ERROR ("Erro ao atualizar status do agendamento. Tente novamente mais tarde."),
    APPOINTMENT_GET_ERROR ("Erro ao recuperar dados do(s) agendamento(s). Tente novamente mais tarde."),
    APPOINTMENT_GET_AVAILABILITY_ERROR ("Erro ao recuperar disponibilidade de agendamento. Tente novamente mais tarde.");

    private String message;

    Message(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }
}