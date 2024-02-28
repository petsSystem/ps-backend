package br.com.petshop.category.model.entity;

import br.com.petshop.category.model.enums.Category;

import java.util.UUID;

public class CategoryEntityMock {
    public static CategoryEntity get() {
        return CategoryEntity.builder()
                .companyId(UUID.randomUUID())
                .type(Category.PETCARE)
                .description("Descrição da Categoria")
                .active(true)
                .build();
    }
}
