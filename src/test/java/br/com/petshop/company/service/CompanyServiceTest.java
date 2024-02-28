package br.com.petshop.company.service;

import br.com.petshop.commons.exception.GenericAlreadyRegisteredException;
import br.com.petshop.company.model.entity.CompanyEntity;
import br.com.petshop.company.model.entity.CompanyEntityMock;
import br.com.petshop.company.repository.CompanyRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

class CompanyServiceTest {
    @Mock CompanyRepository repository;
    @Mock ObjectMapper objectMapper;
    @InjectMocks CompanyService companyService;
    @Mock JsonPatch patch;
    @Mock Pageable paging;
    CompanyEntity entity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        entity = CompanyEntityMock.get();
    }

    @Test
    void testCreate() {
        when(repository.findByCnpj(anyString())).thenReturn(Optional.empty());
        when(repository.save(entity)).thenReturn(entity);

        CompanyEntity result = companyService.create(entity);
        Assertions.assertEquals(entity, result);
    }
    @Test
    void testCreateAlreadyExists() {
        when(repository.findByCnpj(anyString())).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);

        Exception exception = assertThrows(GenericAlreadyRegisteredException.class, () -> {
            companyService.create(entity);
        });
        assertEquals(exception.getMessage(), null);
    }

    @Test
    void testUpdateById() {
        when(repository.save(entity)).thenReturn(entity);

        CompanyEntity result = companyService.updateById(entity);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testActivate() throws JsonPatchException, JsonProcessingException {
        when(repository.save(entity)).thenReturn(entity);

        CompanyEntity result = companyService.activate(entity, patch);
        Assertions.assertEquals(null, result);
    }

    @Test
    void testFindByIdAndActiveIsTrue() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        CompanyEntity result = companyService.findByIdAndActiveIsTrue(id);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testFindAll() {
        Page<CompanyEntity> companies = new PageImpl<>(List.of(entity));
        when(repository.findAll(paging)).thenReturn(companies);

        Page<CompanyEntity> result = companyService.findAll(paging);
        Assertions.assertEquals(companies, result);
    }

    @Test
    void testFindByCompanyIds() {
        UUID id = UUID.randomUUID();
        Page<CompanyEntity> companies = new PageImpl<>(List.of(entity));
        when(repository.findByIdAndActiveIsTrue(any())).thenReturn(Optional.of(entity));

        Page<CompanyEntity> result = companyService.findByCompanyIds(List.of(id), paging);
        Assertions.assertEquals(companies, result);
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        CompanyEntity result = companyService.findById(id);
        Assertions.assertEquals(entity, result);
    }
}