package br.com.petshop.authentication.service;

import br.com.petshop.customer.model.entity.CustomerEntity;
import br.com.petshop.customer.model.entity.CustomerEntityMock;
import br.com.petshop.customer.repository.CustomerRepository;
import br.com.petshop.user.model.entity.UserEntity;
import br.com.petshop.user.model.entity.UserEntityMock;
import br.com.petshop.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    CustomerRepository customerRepository;
    @Mock
    UserRepository systemUserRepository;
    @InjectMocks
    UserService userService;
    UserEntity userEntity;
    CustomerEntity customerEntity;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userEntity = UserEntityMock.get();
        customerEntity = CustomerEntityMock.get();
    }

    @Test
    void testUserDetailsService() {
        when(customerRepository.findByUsernameAndActiveIsTrue(anyString())).thenReturn(null);
        when(systemUserRepository.findByUsernameAndActiveIsTrue(anyString())).thenReturn(Optional.of(userEntity));

        UserDetails result = userService.userDetailsService().loadUserByUsername("sys_username");
        Assertions.assertEquals(result.getUsername(), "99999999999");
    }
    @Test
    void testUserDetailsServicea() {
        when(customerRepository.findByUsernameAndActiveIsTrue(anyString())).thenReturn(Optional.of(customerEntity));
        when(systemUserRepository.findByUsernameAndActiveIsTrue(anyString())).thenReturn(Optional.of(userEntity));

        UserDetails result = userService.userDetailsService().loadUserByUsername("app_username");
        Assertions.assertEquals(result.getUsername(), "99999999999");
    }
}