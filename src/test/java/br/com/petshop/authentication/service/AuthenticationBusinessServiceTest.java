package br.com.petshop.authentication.service;

import br.com.petshop.authentication.model.dto.request.AuthenticationForget;
import br.com.petshop.authentication.model.dto.request.AuthenticationRequest;
import br.com.petshop.authentication.model.dto.response.AuthenticationResponse;
import br.com.petshop.authentication.model.enums.AuthType;
import br.com.petshop.authentication.service.model.dto.request.AuthenticationForgetMock;
import br.com.petshop.authentication.service.model.dto.request.AuthenticationRequestMock;
import br.com.petshop.authentication.service.model.dto.response.AuthenticationResponseMock;
import br.com.petshop.customer.model.dto.request.app.CustomerAppCreateRequestMock;
import br.com.petshop.customer.model.dto.request.app.CustomerAppCreateRequest;
import br.com.petshop.customer.model.dto.response.CustomerResponse;
import br.com.petshop.customer.model.dto.response.CustomerResponseMock;
import br.com.petshop.customer.service.app.CustomerAppBusinessService;
import br.com.petshop.user.service.SysUserBusinessService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class AuthenticationBusinessServiceTest {
    @Mock AuthenticationService service;
    @Mock SysUserBusinessService userBusiness;
    @Mock CustomerAppBusinessService customerBusiness;
    @InjectMocks AuthenticationBusinessService authenticationBusinessService;
    AuthenticationForget forget;
    AuthenticationRequest request;
    AuthenticationResponse response;
    CustomerAppCreateRequest customerAppCreateRequest;
    CustomerResponse customerResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        forget = AuthenticationForgetMock.get();
        request = AuthenticationRequestMock.get();
        response = AuthenticationResponseMock.get();
        customerAppCreateRequest = CustomerAppCreateRequestMock.get();
        customerResponse = CustomerResponseMock.get();
    }

    @Test
    void testLogin() {
        when(service.login(any())).thenReturn(response);

        AuthenticationResponse result = authenticationBusinessService.login(request);
        Assertions.assertEquals(response, result);
    }

    @Test
    void testForgetSys() {
        authenticationBusinessService.forget(forget, AuthType.SYS);
    }

    @Test
    void testForgetApp() {
        authenticationBusinessService.forget(forget, AuthType.APP);
    }

    @Test
    void testCreate() {
        when(customerBusiness.create(any())).thenReturn(customerResponse);

        CustomerResponse result = authenticationBusinessService.create(customerAppCreateRequest);
        Assertions.assertEquals(customerResponse, result);
    }
}