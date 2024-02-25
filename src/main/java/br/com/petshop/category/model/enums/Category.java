package br.com.petshop.category.model.enums;

import lombok.Getter;

import java.util.stream.Stream;

/**
 * Enum das tipos de categorias existentes no sistema.
 */
@Getter
public enum Category {
    PETCARE("Petshop", "Cuidados para o pet"),
    PETVET("Veterinária", "Serviços veterinários"),
    PETFOOD("Alimentação", "Alimentação para pets de estimação"),
    PETSERV("Serviços", "Outros tipos de serviços à disposição");

    private String label;
    private String description;

    Category(String label, String description) {
        this.label = label;
        this.description = description;
    }

    public static Stream<Category> stream() {
        return Stream.of(Category.values());
    }
}
