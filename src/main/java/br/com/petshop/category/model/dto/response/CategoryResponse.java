package br.com.petshop.category.model.dto.response;

import br.com.petshop.category.model.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryResponse implements Serializable {
    private UUID id;
    private Category type;
    private String label;
    private String description;
    private Boolean active;

    public String getLabel() {
        return this.type.getLabel();
    }
}
