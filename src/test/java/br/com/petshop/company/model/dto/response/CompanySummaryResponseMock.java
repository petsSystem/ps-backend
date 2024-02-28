package br.com.petshop.company.model.dto.response;

import br.com.petshop.category.model.enums.Category;

import java.util.List;
import java.util.UUID;

public class CompanySummaryResponseMock {
    public static CompanySummaryResponse get() {
        return CompanySummaryResponse.builder()
                .id(UUID.randomUUID())
                .name("Nome da loja/petshop")
                .phone("11999999999")
                .formattedAddress("Rua dos Tomates, 9999, Jardim dos Legumes, São Paulo - SP, 09999-999, BR")
                .distance(2.5)
                .categories(List.of(Category.PETCARE))
                .build();
    }
}
