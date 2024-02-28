package br.com.petshop.customer.service.sys;

import br.com.petshop.commons.exception.GenericAlreadyRegisteredException;
import br.com.petshop.customer.model.dto.request.app.CustomerChangePasswordRequest;
import br.com.petshop.customer.model.dto.request.app.CustomerChangePasswordRequestMock;
import br.com.petshop.customer.model.entity.CustomerEntity;
import br.com.petshop.customer.model.entity.CustomerEntityMock;
import br.com.petshop.customer.repository.CustomerRepository;
import br.com.petshop.customer.repository.CustomerSpecification;
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
import org.opentest4j.AssertionFailedError;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

class CustomerSysServiceTest {
    @Mock CustomerRepository repository;
    @Mock ObjectMapper objectMapper;
    @Mock CustomerSpecification specification;
    @InjectMocks CustomerSysService customerSysService;
    @Mock JsonPatch patch;
    @Mock Pageable paging;
    CustomerEntity entity;
    CustomerChangePasswordRequest changePassword;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        entity = CustomerEntityMock.get();
        changePassword = CustomerChangePasswordRequestMock.get();
    }

    @Test
    void testCreate() {
        when(repository.findByCpfAndActiveIsTrue(anyString())).thenReturn(Optional.of(entity));

        Exception exception = assertThrows(GenericAlreadyRegisteredException.class, () -> {
            customerSysService.create(entity);
        });
        assertEquals(exception.getMessage(), null);
    }

    @Test
    void testUpdateById() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        CustomerEntity result = customerSysService.updateById(entity);
        Assertions.assertEquals(null, result);
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        CustomerEntity result = customerSysService.findById(id);
        Assertions.assertEquals(entity, result);
    }

//    @Test
    void testAssociateCompanyId() throws JsonPatchException, JsonProcessingException {
        when(repository.save(entity)).thenReturn(entity);

        CustomerEntity result = customerSysService.associateCompanyId(entity, patch);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testFindAllByCompanyId() {
        Page<CustomerEntity> customers = new PageImpl<>(List.of(entity));
        when(repository.findAll(paging)).thenReturn(customers);

        Page<CustomerEntity> result = customerSysService.findAllByCompanyId(UUID.randomUUID(), paging);
        Assertions.assertEquals(null, result);
    }
}