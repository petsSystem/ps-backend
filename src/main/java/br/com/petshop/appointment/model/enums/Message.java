package br.com.petshop.appointment.model.enums;

public enum Message {
    APPOINTMENT_CREATE_ERROR ("Erro ao cadastrar agenda. Tente novamente mais tarde."),
    APPOINTMENT_UPDATE_ERROR ("Erro ao atualizar agenda. Tente novamente mais tarde."),
    APPOINTMENT_CANCEL_ERROR ("Erro ao atualizar agenda. Tente novamente mais tarde."),
    APPOINTMENT_CONFIRM_ERROR ("Erro ao atualizar agenda. Tente novamente mais tarde."),
    APPOINTMENT_GET_ERROR ("Erro ao recuperar dados da(s) agenda(s). Tente novamente mais tarde.");

    private String message;

    Message(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }
}