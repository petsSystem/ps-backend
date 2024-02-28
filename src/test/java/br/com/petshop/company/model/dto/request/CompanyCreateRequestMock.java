package br.com.petshop.company.model.dto.request;

import br.com.petshop.commons.model.Address;

public class CompanyCreateRequestMock {
    public static CompanyCreateRequest get() {
        return CompanyCreateRequest.builder()
                .name("Nome da loja/petshop")
                .cnpj("999999999999999")
                .phone("11999999999")
                .active(false)
                .address(new Address())
                .build();
    }
}