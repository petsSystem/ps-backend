package br.com.petshop.notification;

/**
 * Enum dos tipos de notificação enviados por email.
 */
public enum MailType {
    NEW_PASSWORD("Sua nova senha é:"),
    VALIDATE_EMAIL("Seu token de validação é:"),
    APP_INVITATION("Instale o app PetHound para encontrar o melhor serviço para seu pet próximo à você!");

    private String message;

    MailType(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }
}
