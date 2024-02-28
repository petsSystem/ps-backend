package br.com.petshop.customer.model.dto.response;

import br.com.petshop.commons.model.Address;
import br.com.petshop.customer.model.enums.AppStatus;
import br.com.petshop.customer.model.enums.Origin;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
public class CustomerResponseMock {
    public static CustomerResponse get() {
        return  CustomerResponse.builder()
                .id(UUID.randomUUID())
                .name("Nome do Cliente")
                .cpf("999.999.999-99")
                .email("email.do.cliente@email.com")
                .phone("11999999999")
                .birthDate("01/01/1999")
                .origin(Origin.APP)
                .appStatus(AppStatus.ACTIVE)
                .active(true)
                .companyIds(List.of(UUID.randomUUID()))
                .favorites(List.of(UUID.randomUUID()))
                .address(new Address())
                .username("99999999999")
                .changePassword(false)
                .emailValidated(true)
                .build();
    }
}
