package br.com.petshop.system.category.model.enums;

import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum Category {
    PETCARE("Cuidados para o pet"),
    PETVET("Serviços veterinários"),
    PETFOOD("Alimentação para pets de estimação"),
    PETSERV("Outros tipos de serviços à disposição");

    private String description;

    Category(String description) {
        this.description = description;
    }

    public static Stream<Category> stream() {
        return Stream.of(Category.values());
    }
}
