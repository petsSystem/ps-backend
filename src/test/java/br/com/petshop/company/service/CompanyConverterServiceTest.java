package br.com.petshop.company.service;

import br.com.petshop.company.model.dto.request.CompanyCreateRequest;
import br.com.petshop.company.model.dto.request.CompanyCreateRequestMock;
import br.com.petshop.company.model.dto.request.CompanyUpdateRequest;
import br.com.petshop.company.model.dto.request.CompanyUpdateRequestMock;
import br.com.petshop.company.model.dto.response.CompanyResponse;
import br.com.petshop.company.model.dto.response.CompanyResponseMock;
import br.com.petshop.company.model.dto.response.CompanySummaryResponse;
import br.com.petshop.company.model.dto.response.CompanySummaryResponseMock;
import br.com.petshop.company.model.dto.response.CompanyTableResponse;
import br.com.petshop.company.model.dto.response.CompanyTableResponseMock;
import br.com.petshop.company.model.entity.CompanyEntity;
import br.com.petshop.company.model.entity.CompanyEntityMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class CompanyConverterServiceTest {
    @Mock ModelMapper mapper;
    @Mock Configuration configuration;
    @InjectMocks CompanyConverterService companyConverterService;
    CompanyEntity entity;
    CompanyCreateRequest createRequest;
    CompanyUpdateRequest updateRequest;
    CompanyResponse response;
    CompanySummaryResponse summaryResponse;
    CompanyTableResponse tableResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        entity = CompanyEntityMock.get();
        createRequest = CompanyCreateRequestMock.get();
        updateRequest = CompanyUpdateRequestMock.get();
        response = CompanyResponseMock.get();
        summaryResponse = CompanySummaryResponseMock.get();
        tableResponse = CompanyTableResponseMock.get();
    }

    @Test
    void testCreateRequestIntoEntity() {
        when(mapper.getConfiguration()).thenReturn(configuration);
        when(mapper.map(any(), any())).thenReturn(entity);

        CompanyEntity result = companyConverterService.createRequestIntoEntity(createRequest);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testUpdateRequestIntoEntity() {
        when(mapper.getConfiguration()).thenReturn(configuration);
        when(mapper.map(any(), any())).thenReturn(entity);

        CompanyEntity result = companyConverterService.updateRequestIntoEntity(updateRequest);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testUpdateRequestIntoEntity2() {
        when(mapper.getConfiguration()).thenReturn(configuration);
        when(mapper.map(any(), any())).thenReturn(entity);

        CompanyEntity result = companyConverterService.updateRequestIntoEntity(entity, entity);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testEntityIntoResponse() {
        when(mapper.map(any(), any())).thenReturn(response);

        CompanyResponse result = companyConverterService.entityIntoResponse(entity);
        Assertions.assertEquals(response, result);
    }

    @Test
    void testEntityIntoTableResponse() {
        when(mapper.map(any(), any())).thenReturn(tableResponse);

        CompanyTableResponse result = companyConverterService.entityIntoTableResponse(entity);
        Assertions.assertEquals(tableResponse, result);
    }

    @Test
    void testEntityIntoAppResponse() {
        when(mapper.map(any(), any())).thenReturn(summaryResponse);

        CompanySummaryResponse result = companyConverterService.entityIntoAppResponse(entity);
        Assertions.assertEquals(summaryResponse, result);
    }
}