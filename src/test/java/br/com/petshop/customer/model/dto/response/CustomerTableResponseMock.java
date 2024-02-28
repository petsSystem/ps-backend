package br.com.petshop.customer.model.dto.response;

import java.util.UUID;

public class CustomerTableResponseMock {
    public static CustomerTableResponse get() {
        return CustomerTableResponse.builder()
                .id(UUID.randomUUID())
                .name("Nome do Cliente")
                .cpf("999.999.999-99")
                .email("email.do.cliente@email.com")
                .phone("11999999999")
                .active(true)
                .favorite(false)
                .build();
    }
}
