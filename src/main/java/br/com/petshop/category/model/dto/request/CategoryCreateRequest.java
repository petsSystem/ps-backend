package br.com.petshop.category.model.dto.request;

import br.com.petshop.category.model.enums.Category;
import br.com.petshop.model.Day;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryCreateRequest implements Serializable {
    private Category type;
    private String description;
    private List<Day> days;
    private UUID companyId;
    @Builder.Default
    private Boolean active = true;
}
