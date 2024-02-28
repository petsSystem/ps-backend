package br.com.petshop.authentication.service.model.dto.request;

import br.com.petshop.authentication.model.dto.request.AuthenticationRequest;

public class AuthenticationRequestMock {
    public static AuthenticationRequest get() {
        return AuthenticationRequest.builder()
                .username("username")
                .password("password")
                .build();
    }
}
