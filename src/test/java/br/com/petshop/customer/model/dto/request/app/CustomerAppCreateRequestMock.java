package br.com.petshop.customer.model.dto.request.app;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.customer.model.enums.AppStatus;
import br.com.petshop.customer.model.enums.Origin;

public class CustomerAppCreateRequestMock {
    public static CustomerAppCreateRequest get() {
        return CustomerAppCreateRequest.builder()
                .name("Nome do Cliente")
                .cpf("999.999.999-99")
                .email("email.do.cliente@email.com")
                .birthDate("01/01/1999")
                .password("password")
                .origin(Origin.APP)
                .appStatus(AppStatus.ACTIVE)
                .active(true)
                .changePassword(false)
                .role(Role.USER)
                .emailValidated(false)
                .build();
    }
}
