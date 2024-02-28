package br.com.petshop.authentication.service.model.dto.response;

import br.com.petshop.authentication.model.dto.response.AuthenticationResponse;

public class AuthenticationResponseMock {
    public static AuthenticationResponse get() {
        return new AuthenticationResponse("token");
    }
}
