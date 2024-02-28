package br.com.petshop.category.model.dto.request;

import br.com.petshop.category.model.enums.Category;

public class CategoryUpdateRequestMock {
    public static CategoryUpdateRequest get() {
        return CategoryUpdateRequest.builder()
                .type(Category.PETCARE)
                .description("Descrição da Categoria")
                .build();
    }
}