package br.com.petshop.company.model.dto.request;

import br.com.petshop.commons.model.Address;


public class CompanyUpdateRequestMock {
    public static CompanyUpdateRequest get() {
        return CompanyUpdateRequest.builder()
                .name("Nome da loja/petshop")
                .phone("11999999999")
                .address(new Address())
                .build();
    }
}
