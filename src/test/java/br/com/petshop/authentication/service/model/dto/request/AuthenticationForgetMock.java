package br.com.petshop.authentication.service.model.dto.request;

import br.com.petshop.authentication.model.dto.request.AuthenticationForget;

public class AuthenticationForgetMock {

    public static AuthenticationForget get() {
        return new AuthenticationForget("email@email.com.br");
    }
}
