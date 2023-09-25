package br.com.petshop.authentication.model.enums;

public enum Message {

    //USER MESSAGES
    AUTH_ERROR_USER_PASSWORD("Usuário ou senha estão incorretos."),
    AUTH_ERROR_LOGIN("Erro ao efetuar login. Tente novamente mais tarde."),

    AUTH_NOT_FOUND("Usuário/Email não cadastrado.");

    private String message;

    Message(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }
}