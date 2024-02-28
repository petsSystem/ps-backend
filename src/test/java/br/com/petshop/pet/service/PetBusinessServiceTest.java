package br.com.petshop.pet.service;

import br.com.petshop.pet.model.dto.request.PetCreateRequest;
import br.com.petshop.pet.model.dto.request.PetCreateRequestMock;
import br.com.petshop.pet.model.dto.request.PetUpdateRequest;
import br.com.petshop.pet.model.dto.request.PetUpdateRequestMock;
import br.com.petshop.pet.model.dto.response.PetResponse;
import br.com.petshop.pet.model.dto.response.PetResponseMock;
import br.com.petshop.pet.model.entity.PetEntity;
import br.com.petshop.pet.model.entity.PetEntityMock;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatchException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class PetBusinessServiceTest {
    @Mock PetService service;
    @Mock PetConverterService converter;
    @InjectMocks PetBusinessService petBusinessService;
    @Autowired Principal authentication;
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
    void testGetDogsList() {
        List<String> result = petBusinessService.getDogsList(authentication);
        Assertions.assertEquals(ArrayList.class, result.getClass());
    }

    @Test
    void testGetCatsList() {
        List<String> result = petBusinessService.getCatsList(authentication);
        Assertions.assertEquals(ArrayList.class, result.getClass());
    }

    @Test
    void testCreate() {
        when(service.create(any())).thenReturn(entity);
        when(converter.createRequestIntoEntity(any())).thenReturn(entity);
        when(converter.entityIntoResponse(any())).thenReturn(response);

        PetResponse result = petBusinessService.create(authentication, createRequest);
        Assertions.assertEquals(response, result);
    }

    @Test
    void testUpdate() {
        when(service.findByIdAndActiveIsTrue(any())).thenReturn(entity);
        when(service.updateById(any())).thenReturn(entity);
        when(converter.updateRequestIntoEntity(any())).thenReturn(entity);
        when(converter.updateRequestIntoEntity(any(), any())).thenReturn(entity);
        when(converter.entityIntoResponse(any())).thenReturn(response);

        PetResponse result = petBusinessService.update(authentication, UUID.randomUUID(), updateRequest);
        Assertions.assertEquals(response, result);
    }

    @Test
    void testGetByCustomerId() {
        when(service.getByCustomerId(any())).thenReturn(List.of(entity));
        when(converter.entityIntoResponse(any())).thenReturn(response);

        Set<PetResponse> result = petBusinessService.getByCustomerId(authentication, UUID.randomUUID());
        Assertions.assertEquals(Set.of(response), result);
    }

    @Test
    void testGetById() {
        when(service.getById(any())).thenReturn(entity);
        when(converter.entityIntoResponse(any())).thenReturn(response);

        PetResponse result = petBusinessService.getById(authentication, UUID.randomUUID());
        Assertions.assertEquals(response, result);
    }

    @Test
    void testDeactivate() throws JsonPatchException, JsonProcessingException {
        when(service.getById(any())).thenReturn(entity);
        when(service.deactivate(any(), any())).thenReturn(entity);
        when(converter.entityIntoResponse(any())).thenReturn(response);

        PetResponse result = petBusinessService.deactivate(authentication, UUID.randomUUID(), null);
        Assertions.assertEquals(response, result);
    }
}