package br.com.petshop.company.model.dto.response;

import br.com.petshop.commons.model.Address;

import java.io.Serializable;
import java.util.UUID;

public class CompanyResponseMock implements Serializable {
    public static CompanyResponse get() {
        return CompanyResponse.builder()
                .id(UUID.randomUUID())
                .name("Nome da loja/petshop")
                .cnpj("999999999999999")
                .phone("11999999999")
                .address(new Address())
                .active(true)
                .build();

    }
}
