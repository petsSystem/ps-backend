package br.com.petshop.customer.model.dto.request.sys;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.commons.model.Address;
import br.com.petshop.customer.model.enums.AppStatus;
import br.com.petshop.customer.model.enums.Origin;

import java.util.UUID;

public class CustomerSysCreateRequestMock {
    public static CustomerSysCreateRequest get() {
        return CustomerSysCreateRequest.builder()
                .name("Nome do Cliente")
                .cpf("999.999.999-99")
                .email("email.do.cliente@email.com")
                .phone("11999999999")
                .birthDate("01/01/1999")
                .origin(Origin.SYS)
                .appStatus(AppStatus.PENDING)
                .active(true)
                .companyId(UUID.randomUUID())
                .address(new Address())
                .changePassword(false)
                .role(Role.USER)
                .build();
    }
}
