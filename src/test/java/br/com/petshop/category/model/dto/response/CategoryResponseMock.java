package br.com.petshop.category.model.dto.response;

import br.com.petshop.category.model.enums.Category;

import java.util.UUID;

public class CategoryResponseMock {
    public static CategoryResponse get() {
        return CategoryResponse.builder()
                .id(UUID.randomUUID())
                .type(Category.PETCARE)
                .label("Petshop")
                .description("Descrição da Categoria")
                .active(true)
                .build();
    }
}
