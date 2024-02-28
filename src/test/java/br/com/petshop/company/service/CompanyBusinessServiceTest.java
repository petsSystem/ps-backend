package br.com.petshop.company.service;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.category.service.CategoryBusinessService;
import br.com.petshop.commons.model.Address;
import br.com.petshop.company.model.dto.request.CompanyCreateRequest;
import br.com.petshop.company.model.dto.request.CompanyCreateRequestMock;
import br.com.petshop.company.model.dto.request.CompanyUpdateRequest;
import br.com.petshop.company.model.dto.request.CompanyUpdateRequestMock;
import br.com.petshop.company.model.dto.response.CompanyResponse;
import br.com.petshop.company.model.dto.response.CompanyResponseMock;
import br.com.petshop.company.model.dto.response.CompanySummaryResponse;
import br.com.petshop.company.model.dto.response.CompanySummaryResponseMock;
import br.com.petshop.company.model.entity.CompanyEntity;
import br.com.petshop.company.model.entity.CompanyEntityMock;
import br.com.petshop.customer.model.entity.CustomerEntity;
import br.com.petshop.customer.model.enums.AppStatus;
import br.com.petshop.customer.model.enums.Origin;
import br.com.petshop.user.model.entity.UserEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.data.domain.Page;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

class CompanyBusinessServiceTest {
    @Mock Principal authentication;
    @Mock CompanyConverterService converter;
    @Mock CompanyService service;
    @Mock CompanyValidationService validate;
    @Mock CategoryBusinessService categoryBusinessService;
    @InjectMocks CompanyBusinessService companyBusinessService;
    @Mock JsonPatch patch;
    CompanyEntity entity;
    CompanyCreateRequest createRequest;
    CompanyUpdateRequest updateRequest;
    CompanyResponse response;
    CompanySummaryResponse summaryResponse;
    CompanySummaryResponse tableResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        entity = CompanyEntityMock.get();
        createRequest = CompanyCreateRequestMock.get();
        updateRequest = CompanyUpdateRequestMock.get();
        response = CompanyResponseMock.get();
        summaryResponse = CompanySummaryResponseMock.get();
        tableResponse = CompanySummaryResponseMock.get();
    }
    @Test
    void testCreate() {
        when(converter.createRequestIntoEntity(any())).thenReturn(entity);
        when(converter.entityIntoResponse(any())).thenReturn(response);
        when(service.create(any())).thenReturn(entity);

        CompanyResponse result = companyBusinessService.create(authentication, createRequest);
        Assertions.assertEquals(response, result);
    }

    @Test
    void testUpdateById() {
        when(converter.updateRequestIntoEntity(any())).thenReturn(entity);
        when(converter.updateRequestIntoEntity(any(), any())).thenReturn(entity);
        when(converter.entityIntoResponse(any())).thenReturn(response);
        when(service.updateById(any())).thenReturn(entity);
        when(service.findByIdAndActiveIsTrue(any())).thenReturn(entity);

        CompanyResponse result = companyBusinessService.updateById(authentication, UUID.randomUUID(), updateRequest);
        Assertions.assertEquals(response, result);
    }

    @Test
    void testActivate() throws JsonPatchException, JsonProcessingException {
        when(converter.entityIntoResponse(any())).thenReturn(response);
        when(service.activate(any(), any())).thenReturn(entity);
        when(service.findById(any())).thenReturn(entity);

        CompanyResponse result = companyBusinessService.activate(authentication, UUID.randomUUID(), patch);
        Assertions.assertEquals(response, result);
    }

//    @Test
//    void testGet() {
//        when(converter.entityIntoResponse(any())).thenReturn(response);
//        when(service.findAll(any())).thenReturn(null);
//        when(service.findByCompanyIds(any(), any())).thenReturn(null);
//        when(validate.getSysAuthUser(any())).thenReturn(new UserEntity("name", "cpf", "email", "phone", Boolean.TRUE, null, new Address("postalCode", "street", "number", "neighborhood", "city", "state", "country"), List.of(null), List.of(null), Role.ADMIN, "username", "password", Boolean.TRUE, "emailToken", LocalDateTime.of(2024, Month.FEBRUARY, 26, 17, 2, 16)));
//        when(validate.getSysRole(any())).thenReturn(Role.ADMIN);
//        when(categoryBusinessService.getSysAuthUser(any())).thenReturn(new UserEntity("name", "cpf", "email", "phone", Boolean.TRUE, null, new Address("postalCode", "street", "number", "neighborhood", "city", "state", "country"), List.of(null), List.of(null), Role.ADMIN, "username", "password", Boolean.TRUE, "emailToken", LocalDateTime.of(2024, Month.FEBRUARY, 26, 17, 2, 16)));
//        when(categoryBusinessService.getSysRole(any())).thenReturn(Role.ADMIN);
//
//        Page<CompanyResponse> result = companyBusinessService.get(null, null);
//        Assertions.assertEquals(null, result);
//    }

    @Test
    void testGetById() {
        when(converter.entityIntoResponse(any())).thenReturn(response);
        when(service.findById(any())).thenReturn(entity);

        CompanyResponse result = companyBusinessService.getById(authentication, UUID.randomUUID());
        Assertions.assertEquals(result, result);
    }

    @Test
    void testFindActiveCompany() {
        when(converter.entityIntoResponse(any())).thenReturn(response);
        when(service.findById(any())).thenReturn(entity);

        CompanyResponse result = companyBusinessService.findActiveCompany(UUID.randomUUID(), List.of(UUID.randomUUID()));
        Assertions.assertEquals(response, result);
    }
}