package br.com.petshop.customer.service.app;

import br.com.petshop.commons.service.AuthenticationCommonService;
import br.com.petshop.customer.model.dto.request.app.CustomerAppCreateRequest;
import br.com.petshop.customer.model.dto.request.app.CustomerAppCreateRequestMock;
import br.com.petshop.customer.model.dto.request.app.CustomerAppUpdateRequest;
import br.com.petshop.customer.model.dto.request.app.CustomerAppUpdateRequestMock;
import br.com.petshop.customer.model.dto.request.app.CustomerChangePasswordRequest;
import br.com.petshop.customer.model.dto.request.app.CustomerChangePasswordRequestMock;
import br.com.petshop.customer.model.dto.request.app.EmailValidateRequest;
import br.com.petshop.customer.model.dto.request.app.EmailValidateRequestMock;
import br.com.petshop.customer.model.dto.response.CustomerResponse;
import br.com.petshop.customer.model.dto.response.CustomerResponseMock;
import br.com.petshop.customer.model.entity.CustomerEntity;
import br.com.petshop.customer.model.entity.CustomerEntityMock;
import br.com.petshop.notification.MailNotificationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;

import java.security.Principal;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

class CustomerAppBusinessServiceTest {
    @Mock CustomerAppService service;
    @Mock MailNotificationService mailService;
    @Mock CustomerAppConverterService converter;
    @InjectMocks @Spy CustomerAppBusinessService customerAppBusinessService;
    @Mock JsonPatch patch;
    @Mock AuthenticationCommonService commonService;
    CustomerEntity entity;
    CustomerAppCreateRequest createRequest;
    CustomerAppUpdateRequest updateRequest;
    CustomerChangePasswordRequest changePassword;
    EmailValidateRequest emailValidateRequest;
    CustomerResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        entity = CustomerEntityMock.get();
        createRequest = CustomerAppCreateRequestMock.get();
        updateRequest = CustomerAppUpdateRequestMock.get();
        changePassword = CustomerChangePasswordRequestMock.get();
        emailValidateRequest = EmailValidateRequestMock.get();
        response = CustomerResponseMock.get();
    }

    @Test
    void testCreate() {
        when(service.create(any())).thenReturn(entity);
        when(converter.createRequestIntoEntity(any())).thenReturn(entity);
        when(converter.entityIntoResponse(any())).thenReturn(response);

        CustomerResponse result = customerAppBusinessService.create(createRequest);
        Assertions.assertEquals(response, result);
    }

    @Test
    void testForget() {
        when(service.findByCpfAndActiveIsTrue(anyString())).thenReturn(entity);
        when(service.forget(any())).thenReturn(entity);

        customerAppBusinessService.forget("99999999999");
    }

    @Test
    void testUpdate() {
        UsernamePasswordAuthenticationToken userAuthentication = Mockito.mock(UsernamePasswordAuthenticationToken.class);
        when(userAuthentication.getPrincipal()).thenReturn(entity);
        when(service.findById(any())).thenReturn(entity);
        when(service.updateById(any())).thenReturn(entity);
        when(converter.updateRequestIntoEntity(any())).thenReturn(entity);
        when(converter.updateRequestIntoEntity(any(), any())).thenReturn(entity);
        when(converter.entityIntoResponse(any())).thenReturn(response);

        CustomerResponse result = customerAppBusinessService.update(userAuthentication, updateRequest);
        Assertions.assertEquals(response, result);
    }

    @Test
    void testChangePassword() {
        UsernamePasswordAuthenticationToken userAuthentication = Mockito.mock(UsernamePasswordAuthenticationToken.class);
        when(userAuthentication.getPrincipal()).thenReturn(entity);
        when(service.findByCpfAndActiveIsTrue(anyString())).thenReturn(entity);
        when(service.changePassword(any(), any())).thenReturn(entity);
        when(converter.entityIntoResponse(any())).thenReturn(response);

        CustomerResponse result = customerAppBusinessService.changePassword(userAuthentication, changePassword);
        Assertions.assertEquals(response, result);
    }

    @Test
    void testMe() {
        UsernamePasswordAuthenticationToken userAuthentication = Mockito.mock(UsernamePasswordAuthenticationToken.class);
        when(userAuthentication.getPrincipal()).thenReturn(entity);
        when(service.findById(any())).thenReturn(entity);
        when(converter.entityIntoResponse(any())).thenReturn(response);

        CustomerResponse result = customerAppBusinessService.me(userAuthentication);
        Assertions.assertEquals(response, result);
    }

    @Test
    void testFavorite() throws JsonPatchException, JsonProcessingException {
        UsernamePasswordAuthenticationToken userAuthentication = Mockito.mock(UsernamePasswordAuthenticationToken.class);
        when(userAuthentication.getPrincipal()).thenReturn(entity);
        when(service.findById(any())).thenReturn(entity);
        when(service.associateCompanyId(any(), any())).thenReturn(entity);
        when(converter.entityIntoResponse(any())).thenReturn(response);

        CustomerResponse result = customerAppBusinessService.favorite(userAuthentication, null);
        Assertions.assertEquals(response, result);
    }

    @Test
    void testUnfavorite() throws JsonPatchException, JsonProcessingException {
        UsernamePasswordAuthenticationToken userAuthentication = Mockito.mock(UsernamePasswordAuthenticationToken.class);
        when(userAuthentication.getPrincipal()).thenReturn(entity);
        when(service.findById(any())).thenReturn(entity);
        when(service.desassociateCompanyId(any(), any())).thenReturn(entity);
        when(converter.entityIntoResponse(any())).thenReturn(response);

        CustomerResponse result = customerAppBusinessService.unfavorite(userAuthentication, null);
        Assertions.assertEquals(response, result);
    }

    @Test
    @WithMockUser
    void testEmailValidate() {
        UsernamePasswordAuthenticationToken userAuthentication = Mockito.mock(UsernamePasswordAuthenticationToken.class);
        when(userAuthentication.getPrincipal()).thenReturn(entity);
        when(service.findByCpfAndActiveIsTrue(anyString())).thenReturn(entity);
        when(service.emailValidate(any(), anyString())).thenReturn(entity);
        when(converter.entityIntoResponse(any())).thenReturn(response);

        CustomerResponse result = customerAppBusinessService.emailValidate(userAuthentication, emailValidateRequest);
        Assertions.assertEquals(response, result);
    }

    @Test
    void testResendEmailValidate() {
        UsernamePasswordAuthenticationToken userAuthentication = Mockito.mock(UsernamePasswordAuthenticationToken.class);
        when(userAuthentication.getPrincipal()).thenReturn(entity);
        when(service.findByCpfAndActiveIsTrue(anyString())).thenReturn(entity);

        customerAppBusinessService.resendEmailValidate(userAuthentication);
    }

    @Test
    void testDeactivate() {
        UsernamePasswordAuthenticationToken userAuthentication = Mockito.mock(UsernamePasswordAuthenticationToken.class);
        when(userAuthentication.getPrincipal()).thenReturn(entity);
        when(service.findByCpfAndActiveIsTrue(anyString())).thenReturn(entity);

        customerAppBusinessService.deactivate(userAuthentication, null);
    }
}