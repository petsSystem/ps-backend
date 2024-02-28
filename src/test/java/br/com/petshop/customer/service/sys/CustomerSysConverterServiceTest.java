package br.com.petshop.customer.service.sys;

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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CustomerSysConverterServiceTest {
    @Mock ModelMapper mapper;
    @Mock Configuration configuration;
    @InjectMocks CustomerSysConverterService customerSysConverterService;
    CustomerEntity entity;
    CustomerSysCreateRequest createRequest;
    CustomerSysUpdateRequest updateRequest;
    CustomerResponse response;
    CustomerTableResponse tableResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        entity = CustomerEntityMock.get();
        createRequest = CustomerSysCreateRequestMock.get();
        updateRequest = CustomerSysUpdateRequestMock.get();
        response = CustomerResponseMock.get();
        tableResponse = CustomerTableResponseMock.get();
    }

    @Test
    void testCreateRequestIntoEntity() {
        when(mapper.getConfiguration()).thenReturn(configuration);
        when(mapper.map(any(), any())).thenReturn(entity);

        CustomerEntity result = customerSysConverterService.createRequestIntoEntity(createRequest);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testUpdateRequestIntoEntity() {
        when(mapper.getConfiguration()).thenReturn(configuration);
        when(mapper.map(any(), any())).thenReturn(entity);

        CustomerEntity result = customerSysConverterService.updateRequestIntoEntity(updateRequest);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testUpdateRequestIntoEntity2() {
        when(mapper.getConfiguration()).thenReturn(configuration);
        when(mapper.map(any(), any())).thenReturn(entity);

        CustomerEntity result = customerSysConverterService.updateRequestIntoEntity(entity, entity);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testEntityIntoResponse() {
        when(mapper.map(any(), any())).thenReturn(response);

        CustomerResponse result = customerSysConverterService.entityIntoResponse(entity);
        Assertions.assertEquals(response, result);
    }

    @Test
    void testEntityIntoTableResponse() {
        when(mapper.map(any(), any())).thenReturn(tableResponse);

        CustomerTableResponse result = customerSysConverterService.entityIntoTableResponse(entity);
        Assertions.assertEquals(tableResponse, result);
    }
}