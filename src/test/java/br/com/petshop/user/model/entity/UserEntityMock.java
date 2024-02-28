package br.com.petshop.user.model.entity;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.commons.model.Address;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class UserEntityMock {
    public static UserEntity get() {
        return UserEntity.builder()
                .name("Nome do Usuário")
                .cpf("999.999.999-99")
                .email("email.do.usuario@email.com")
                .phone("11999999999")
                .active(true)
                .currentCompanyId(UUID.randomUUID())
                .address(new Address())
                .companyIds(List.of(UUID.randomUUID()))
                .profileIds(List.of(UUID.randomUUID()))
                .role(Role.OWNER)
                .username("99999999999")
                .password("password")
                .changePassword(false)
                .emailToken("token")
                .emailTokenTime(LocalDateTime.now())
                .build();
    }
}
