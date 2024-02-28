package br.com.petshop.customer.service.app;

import br.com.petshop.customer.model.dto.request.app.CustomerAppCreateRequest;
import br.com.petshop.customer.model.dto.request.app.CustomerAppCreateRequestMock;
import br.com.petshop.customer.model.dto.request.app.CustomerAppUpdateRequest;
import br.com.petshop.customer.model.dto.request.app.CustomerAppUpdateRequestMock;
import br.com.petshop.customer.model.dto.response.CustomerResponse;
import br.com.petshop.customer.model.dto.response.CustomerResponseMock;
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

class CustomerAppConverterServiceTest {
    @Mock ModelMapper mapper;
    @Mock Configuration configuration;
    @InjectMocks CustomerAppConverterService customerAppConverterService;
    CustomerEntity entity;
    CustomerAppCreateRequest createRequest;
    CustomerAppUpdateRequest updateRequest;
    CustomerResponse response;

    @BeforeEach void setUp() {
        MockitoAnnotations.openMocks(this);
        entity = CustomerEntityMock.get();
        createRequest = CustomerAppCreateRequestMock.get();
        updateRequest = CustomerAppUpdateRequestMock.get();
        response = CustomerResponseMock.get();
    }

    @Test
    void testCreateRequestIntoEntity() {
        when(mapper.getConfiguration()).thenReturn(configuration);
        when(mapper.map(any(), any())).thenReturn(entity);
        CustomerEntity result = customerAppConverterService.createRequestIntoEntity(createRequest);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testUpdateRequestIntoEntity() {
        when(mapper.getConfiguration()).thenReturn(configuration);
        when(mapper.map(any(), any())).thenReturn(entity);
        CustomerEntity result = customerAppConverterService.updateRequestIntoEntity(updateRequest);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testUpdateRequestIntoEntity2() {
        when(mapper.getConfiguration()).thenReturn(configuration);
        when(mapper.map(any(), any())).thenReturn(entity);
        CustomerEntity result = customerAppConverterService.updateRequestIntoEntity(entity, entity);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testEntityIntoResponse() {
        when(mapper.map(any(), any())).thenReturn(response);
        CustomerResponse result = customerAppConverterService.entityIntoResponse(entity);
        Assertions.assertEquals(response, result);
    }
}