package br.com.petshop.pet.service;

import br.com.petshop.pet.model.dto.request.PetCreateRequest;
import br.com.petshop.pet.model.dto.request.PetCreateRequestMock;
import br.com.petshop.pet.model.dto.request.PetUpdateRequest;
import br.com.petshop.pet.model.dto.request.PetUpdateRequestMock;
import br.com.petshop.pet.model.dto.response.PetResponse;
import br.com.petshop.pet.model.dto.response.PetResponseMock;
import br.com.petshop.pet.model.entity.PetEntity;
import br.com.petshop.pet.model.entity.PetEntityMock;
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

class PetConverterServiceTest {
    @Mock ModelMapper mapper;
    @Mock Configuration configuration;
    @InjectMocks PetConverterService petConverterService;
    PetEntity entity;
    PetCreateRequest createRequest;
    PetUpdateRequest updateRequest;
    PetResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        entity = PetEntityMock.get();
        createRequest = PetCreateRequestMock.get();
        updateRequest = PetUpdateRequestMock.get();
        response = PetResponseMock.get();
    }

    @Test
    void testCreateRequestIntoEntity() {
        when(mapper.getConfiguration()).thenReturn(configuration);
        when(mapper.map(any(), any())).thenReturn(entity);

        PetEntity result = petConverterService.createRequestIntoEntity(createRequest);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testUpdateRequestIntoEntity() {
        when(mapper.getConfiguration()).thenReturn(configuration);
        when(mapper.map(any(), any())).thenReturn(entity);

        PetEntity result = petConverterService.updateRequestIntoEntity(updateRequest);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testUpdateRequestIntoEntity2() {
        when(mapper.getConfiguration()).thenReturn(configuration);
        when(mapper.map(any(), any())).thenReturn(entity);

        PetEntity result = petConverterService.updateRequestIntoEntity(entity, entity);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testEntityIntoResponse() {
        when(mapper.map(any(), any())).thenReturn(response);

        PetResponse result = petConverterService.entityIntoResponse(entity);
        Assertions.assertEquals(response, result);
    }
}