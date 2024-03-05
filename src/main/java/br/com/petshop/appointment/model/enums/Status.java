package br.com.petshop.appointment.model.enums;

/**
 * Enum do status do agendamento
 */
public enum Status {
    SCHEDULED,
    CONFIRMED,
    CANCELLED_BY_CLIENT,
    CANCELLED_BY_PETSHOP,
    IN_PROGRESS,
    COMPLETED,
    DID_NOT_SHOW_UP,
    RESCHEDULED,
    AWAITING_CONFIRMATION,
    CONCLUDED;
}
