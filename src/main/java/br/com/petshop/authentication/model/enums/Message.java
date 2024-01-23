package br.com.petshop.authentication.model.enums;

public enum Message {

    AUTH_USER_PASSWORD_ERROR("Usuário ou senha estão incorretos."),
    AUTH_LOGIN_ERROR("Erro ao efetuar login. Tente novamente mais tarde."),
    AUTH_NOT_FOUND_ERROR ("Usuário não encontrado.");

    private String message;

    Message(String message) {
        this.message = message;
    }

    public String get() {
        return message;
    }
}