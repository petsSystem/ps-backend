package br.com.petshop.company.model.dto.response;

import java.util.UUID;

public class CompanyTableResponseMock {
    public static CompanyTableResponse get() {
        return CompanyTableResponse.builder()
                .id(UUID.randomUUID())
                .name("Nome da loja/petshop")
                .cnpj("999999999999999")
                .phone("11999999999")
                .active(true)
                .build();

    }
}
