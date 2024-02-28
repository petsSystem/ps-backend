package br.com.petshop.category.service;

import br.com.petshop.category.model.dto.request.CategoryUpdateRequest;
import br.com.petshop.category.model.dto.request.CategoryUpdateRequestMock;
import br.com.petshop.category.model.dto.response.CategoryResponse;
import br.com.petshop.category.model.dto.response.CategoryResponseMock;
import br.com.petshop.category.model.entity.CategoryEntity;
import br.com.petshop.category.model.entity.CategoryEntityMock;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.when;

class CategoryBusinessServiceTest {
    @Mock CategoryService service;
    @Mock CategoryValidateService validate;
    @Mock CategoryConverterService converter;
    @InjectMocks CategoryBusinessService categoryBusinessService;
    @Mock Principal authentication;
    @Mock JsonPatch patch;
    CategoryEntity entity;
    CategoryUpdateRequest request;
    CategoryResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        entity = CategoryEntityMock.get();
        request = CategoryUpdateRequestMock.get();
        response = CategoryResponseMock.get();
    }

    @Test
    void testCreateAutomatic() {
        when(service.save(any())).thenReturn(entity);

        categoryBusinessService.createAutomatic(UUID.randomUUID());
    }

    @Test
    void testUpdateById() {
        when(service.updateById(any())).thenReturn(entity);
        when(service.findByIdAndActiveIsTrue(any())).thenReturn(entity);
        when(converter.updateRequestIntoEntity(any())).thenReturn(entity);
        when(converter.updateRequestIntoEntity(any(), any())).thenReturn(entity);
        when(converter.entityIntoResponse(any())).thenReturn(response);

        CategoryResponse result = categoryBusinessService.updateById(authentication, UUID.randomUUID(), request);
        Assertions.assertEquals(response, result);
    }

    @Test
    void testActivate() throws JsonPatchException, JsonProcessingException {
        when(service.activate(any(), any())).thenReturn(entity);
        when(service.findById(any())).thenReturn(entity);
        when(converter.entityIntoResponse(any())).thenReturn(response);

        CategoryResponse result = categoryBusinessService.activate(authentication, UUID.randomUUID(), patch);
        Assertions.assertEquals(response, result);
    }

    @Test
    void testGetAllByCompanyId() {
        when(service.findAllByCompanyId(any(), anyBoolean())).thenReturn(List.of(entity));
        when(converter.entityIntoResponse(any())).thenReturn(response);

        List<CategoryResponse> result = categoryBusinessService.getAllByCompanyId(authentication, UUID.randomUUID(), Boolean.TRUE);
        Assertions.assertEquals(List.of(response), result);
    }

    @Test
    void testGetById() {
        when(service.findById(any())).thenReturn(entity);
        when(converter.entityIntoResponse(any())).thenReturn(response);

        CategoryResponse result = categoryBusinessService.getById(authentication, UUID.randomUUID());
        Assertions.assertEquals(response, result);
    }
}