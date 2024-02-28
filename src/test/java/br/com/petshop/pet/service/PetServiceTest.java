package br.com.petshop.pet.service;

import br.com.petshop.pet.model.entity.PetEntity;
import br.com.petshop.pet.model.entity.PetEntityMock;
import br.com.petshop.pet.repository.PetRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class PetServiceTest {
    @Mock PetRepository repository;
    @Mock ObjectMapper objectMapper;
    @InjectMocks PetService petService;
    @Mock JsonPatch patch;
    PetEntity entity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        entity = PetEntityMock.get();
    }

    @Test
    void testCreate() {
        when(repository.save(entity)).thenReturn(entity);

        PetEntity result = petService.create(entity);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testFindByIdAndActiveIsTrue() {
        when(repository.findByIdAndActiveIsTrue(any())).thenReturn(Optional.of(entity));

        PetEntity result = petService.findByIdAndActiveIsTrue(UUID.randomUUID());
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testUpdateById() {
        when(repository.save(entity)).thenReturn(entity);

        PetEntity result = petService.updateById(entity);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testGetByCustomerId() {
        when(repository.findByCustomerIdAndActiveIsTrue(any())).thenReturn(List.of(entity));

        List<PetEntity> result = petService.getByCustomerId(UUID.randomUUID());
        Assertions.assertEquals(List.of(entity), result);
    }

    @Test
    void testGetById() {
        when(repository.findByIdAndActiveIsTrue(any())).thenReturn(Optional.of(entity));

        PetEntity result = petService.getById(UUID.randomUUID());
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testDeactivate() throws JsonPatchException, JsonProcessingException {
        when(repository.save(entity)).thenReturn(entity);

        PetEntity result = petService.deactivate(entity, patch);
        Assertions.assertEquals(null, result);
    }
}