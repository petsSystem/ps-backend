package br.com.petshop.system.category.model.dto.response;

import br.com.petshop.system.category.model.enums.Category;
import br.com.petshop.system.model.Day;
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
public class CategoryResponse implements Serializable {
    private UUID id;
    private Category type;
    private String label;
    private List<Day> days;
    private Boolean active;
}
