package br.com.petshop.customer.service.app;

import br.com.petshop.commons.exception.GenericAlreadyRegisteredException;
import br.com.petshop.customer.model.dto.request.app.CustomerChangePasswordRequest;
import br.com.petshop.customer.model.dto.request.app.CustomerChangePasswordRequestMock;
import br.com.petshop.customer.model.entity.CustomerEntity;
import br.com.petshop.customer.model.entity.CustomerEntityMock;
import br.com.petshop.customer.model.enums.AppStatus;
import br.com.petshop.customer.repository.CustomerRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

class CustomerAppServiceTest {
    @Mock CustomerRepository repository;
    @Mock ObjectMapper objectMapper;
    @Mock PasswordEncoder passwordEncoder;
    @InjectMocks CustomerAppService customerAppService;
    @Mock JsonPatch patch;
    CustomerEntity entity;
    CustomerChangePasswordRequest changePassword;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        entity = CustomerEntityMock.get();
        changePassword = CustomerChangePasswordRequestMock.get();
    }

    @Test
    void testCreateAlreadyExists() {
        when(repository.findByCpfAndActiveIsTrue(anyString())).thenReturn(Optional.of(entity));

        Exception exception = assertThrows(GenericAlreadyRegisteredException.class, () -> {
            customerAppService.create(entity);
        });
        assertEquals(exception.getMessage(), null);
    }

    @Test
    void testCreate() {
        entity.setAppStatus(AppStatus.PENDING);
        when(repository.findByCpfAndActiveIsTrue(anyString())).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);

        CustomerEntity result = customerAppService.create(entity);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testFindByCpfAndActiveIsTrue() {
        when(repository.findByCpfAndActiveIsTrue(anyString())).thenReturn(Optional.of(entity));

        CustomerEntity result = customerAppService.findByCpfAndActiveIsTrue("username");
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        CustomerEntity result = customerAppService.findById(id);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testForget() {
        when(repository.save(entity)).thenReturn(entity);

        CustomerEntity result = customerAppService.forget(entity);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testUpdateById() {
        when(repository.save(entity)).thenReturn(entity);

        CustomerEntity result = customerAppService.updateById(entity);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testChangePassword() {
        entity.setPassword("$2a$10$icpZ6DZBpruvc2Hhbl4bKuEZKszqyG9PTG3aO94Tc5qQ7eW6zSv4.");
        when(repository.save(entity)).thenReturn(entity);

        CustomerEntity result = customerAppService.changePassword(entity, changePassword);
        Assertions.assertEquals(entity, result);
    }

//    @Test
//    void testAssociateCompanyId() throws JsonPatchException, JsonProcessingException {
//        when(repository.save(entity)).thenReturn(entity);
//
//        CustomerEntity result = customerAppService.associateCompanyId(entity, patch);
//        Assertions.assertEquals(entity, result);
//    }

//    @Test
//    void testDesassociateCompanyId() throws JsonPatchException, JsonProcessingException {
//        when(repository.save(entity)).thenReturn(entity);
//
//        CustomerEntity result = customerAppService.desassociateCompanyId(entity, patch);
//        Assertions.assertEquals(entity, result);
//    }

    @Test
    void testEmailValidate() {
        when(repository.save(entity)).thenReturn(entity);

        CustomerEntity result = customerAppService.emailValidate(entity, "token");
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testDeactivate() {
        when(repository.save(entity)).thenReturn(entity);

        customerAppService.deactivate(entity);
    }
}