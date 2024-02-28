package br.com.petshop.company.model.entity;

import br.com.petshop.commons.model.Address;

public class CompanyEntityMock {
    public static CompanyEntity get() {
        return CompanyEntity.builder()
                .name("Nome da loja/petshop")
                .cnpj("999999999999999")
                .phone("11999999999")
                .active(true)
                .address(new Address())
                .build();
    }
}
