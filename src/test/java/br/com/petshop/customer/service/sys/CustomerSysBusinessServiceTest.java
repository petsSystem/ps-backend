package br.com.petshop.customer.service.sys;

import br.com.petshop.customer.model.dto.request.app.CustomerChangePasswordRequest;
import br.com.petshop.customer.model.dto.request.app.CustomerChangePasswordRequestMock;
import br.com.petshop.customer.model.dto.request.app.EmailValidateRequest;
import br.com.petshop.customer.model.dto.request.app.EmailValidateRequestMock;
import br.com.petshop.customer.model.dto.request.sys.CustomerSysCreateRequest;
import br.com.petshop.customer.model.dto.request.sys.CustomerSysCreateRequestMock;
import br.com.petshop.customer.model.dto.request.sys.CustomerSysUpdateRequest;
import br.com.petshop.customer.model.dto.request.sys.CustomerSysUpdateRequestMock;
import br.com.petshop.customer.model.dto.response.CustomerResponse;
import br.com.petshop.customer.model.dto.response.CustomerResponseMock;
import br.com.petshop.customer.model.dto.response.CustomerTableResponse;
import br.com.petshop.customer.model.dto.response.CustomerTableResponseMock;
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
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class CustomerSysBusinessServiceTest {
    @Mock CustomerSysService service;
    @Mock MailNotificationService mailService;
    @Mock CustomerSysConverterService converter;
    @InjectMocks CustomerSysBusinessService customerSysBusinessService;
    @Autowired Principal authentication;
    @Mock JsonPatch patch;
    @Mock Pageable paging;
    CustomerEntity entity;
    CustomerSysCreateRequest createRequest;
    CustomerSysUpdateRequest updateRequest;
    CustomerChangePasswordRequest changePassword;
    EmailValidateRequest emailValidateRequest;
    CustomerResponse response;
    CustomerTableResponse tableResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        entity = CustomerEntityMock.get();
        createRequest = CustomerSysCreateRequestMock.get();
        updateRequest = CustomerSysUpdateRequestMock.get();
        changePassword = CustomerChangePasswordRequestMock.get();
        emailValidateRequest = EmailValidateRequestMock.get();
        response = CustomerResponseMock.get();
        tableResponse = CustomerTableResponseMock.get();
    }

    @Test
    void testCreate() {
        when(service.create(any())).thenReturn(entity);
        when(converter.createRequestIntoEntity(any())).thenReturn(entity);
        when(converter.entityIntoResponse(any())).thenReturn(response);

        CustomerResponse result = customerSysBusinessService.create(createRequest);
        Assertions.assertEquals(response, result);
    }

    @Test
    void testUpdate() {
        when(service.updateById(any())).thenReturn(entity);
        when(service.findById(any())).thenReturn(entity);
        when(converter.updateRequestIntoEntity(any())).thenReturn(entity);
        when(converter.updateRequestIntoEntity(any(), any())).thenReturn(entity);
        when(converter.entityIntoResponse(any())).thenReturn(response);

        CustomerResponse result = customerSysBusinessService.update(authentication, UUID.randomUUID(), updateRequest);
        Assertions.assertEquals(response, result);
    }

    @Test
    void testAssociateCompanyId() throws JsonPatchException, JsonProcessingException {
        when(service.findById(any())).thenReturn(entity);
        when(service.associateCompanyId(any(), any())).thenReturn(entity);
        when(converter.entityIntoResponse(any())).thenReturn(response);

        CustomerResponse result = customerSysBusinessService.associateCompanyId(authentication, UUID.randomUUID(), patch);
        Assertions.assertEquals(response, result);
    }

    @Test
    void testGet() {
        when(service.findAllByCompanyId(any(), any())).thenReturn(new PageImpl<>(List.of(entity)));
        when(converter.entityIntoTableResponse(any())).thenReturn(tableResponse);

        Page<CustomerTableResponse> result = customerSysBusinessService.get(authentication, UUID.randomUUID(), paging);
        Assertions.assertEquals(new PageImpl<>(List.of(tableResponse)), result);
    }

    @Test
    void testGetById() {
        when(service.findById(any())).thenReturn(entity);
        when(converter.entityIntoResponse(any())).thenReturn(response);

        CustomerResponse result = customerSysBusinessService.getById(authentication, UUID.randomUUID());
        Assertions.assertEquals(response, result);
    }
}