package br.com.petshop.customer.model.dto.request.app;

import br.com.petshop.commons.model.Address;

public class CustomerAppUpdateRequestMock {
    public static CustomerAppUpdateRequest get() {
        return CustomerAppUpdateRequest.builder()
                .name("Nome do Cliente")
                .email("email.do.cliente@email.com")
                .phone("11999999999")
                .birthDate("01/01/1999")
                .address(new Address())
                .build();
    }
}
