package br.com.petshop.authentication.service;

import br.com.petshop.authentication.model.dto.request.AuthenticationRequest;
import br.com.petshop.authentication.model.dto.response.AuthenticationResponse;
import br.com.petshop.authentication.service.model.dto.request.AuthenticationRequestMock;
import br.com.petshop.authentication.service.model.dto.response.AuthenticationResponseMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class AuthenticationServiceTest {
    @Mock JwtService jwtService;
    @Mock AuthenticationManager authenticationManager;
    @InjectMocks AuthenticationService authenticationService;
    @Mock Authentication auth;
    AuthenticationRequest request;
    AuthenticationResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        request = AuthenticationRequestMock.get();
        response = AuthenticationResponseMock.get();
    }

    @Test
    void testLogin() {
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(jwtService.generateToken(any())).thenReturn("token");

        AuthenticationResponse result = authenticationService.login(request);
        Assertions.assertEquals(response, result);
    }
}