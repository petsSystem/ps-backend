package br.com.petshop.category.service;

import br.com.petshop.category.model.dto.request.CategoryUpdateRequest;
import br.com.petshop.category.model.dto.request.CategoryUpdateRequestMock;
import br.com.petshop.category.model.dto.response.CategoryResponse;
import br.com.petshop.category.model.dto.response.CategoryResponseMock;
import br.com.petshop.category.model.entity.CategoryEntity;
import br.com.petshop.category.model.entity.CategoryEntityMock;
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

class CategoryConverterServiceTest {
    @Mock ModelMapper mapper;
    @Mock Configuration configuration;
    @InjectMocks CategoryConverterService categoryConverterService;
    CategoryEntity entity;
    CategoryUpdateRequest updateRequest;
    CategoryResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        entity = CategoryEntityMock.get();
        updateRequest = CategoryUpdateRequestMock.get();
        response = CategoryResponseMock.get();
    }

    @Test
    void testUpdateRequestIntoEntity() {
        when(mapper.getConfiguration()).thenReturn(configuration);
        when(mapper.map(any(), any())).thenReturn(entity);
        CategoryEntity result = categoryConverterService.updateRequestIntoEntity(updateRequest);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testUpdateRequestIntoEntity2() {
        when(mapper.getConfiguration()).thenReturn(configuration);
        when(mapper.map(any(), any())).thenReturn(entity);
        CategoryEntity result = categoryConverterService.updateRequestIntoEntity(entity, entity);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testEntityIntoResponse() {
        when(mapper.map(any(), any())).thenReturn(response);
        CategoryResponse result = categoryConverterService.entityIntoResponse(entity);
        Assertions.assertEquals(response, result);
    }
}