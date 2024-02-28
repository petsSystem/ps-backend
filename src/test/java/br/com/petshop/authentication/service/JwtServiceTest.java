package br.com.petshop.authentication.service;

import br.com.petshop.user.model.entity.UserEntity;
import br.com.petshop.user.model.entity.UserEntityMock;
import io.jsonwebtoken.MalformedJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
class JwtServiceTest {
    JwtService jwtService;
    UserEntity userEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "jwtSigningKey", "413F4428472B4B6250655368566D5970337336763979244226452948404D6351");

        userEntity = UserEntityMock.get();
    }

    @Test
    void testExtractUserName() throws IllegalAccessException, NoSuchFieldException {
        Exception exception = assertThrows(MalformedJwtException.class, () -> {
            jwtService.extractUserName("token");
        });
        assertEquals(exception.getMessage(), "JWT strings must contain exactly 2 period characters. Found: 0");
    }

    @Test
    void testGenerateToken() {
        String result = jwtService.generateToken(userEntity);
        assertEquals(139, result.length());
    }

    @Test
    void testIsTokenValid() {
        Exception exception = assertThrows(MalformedJwtException.class, () -> {
            jwtService.isTokenValid("token", userEntity);
        });
        assertEquals(exception.getMessage(), "JWT strings must contain exactly 2 period characters. Found: 0");
    }
}