package br.com.petshop.system.category.model.dto.request;

import br.com.petshop.system.category.model.enums.Category;
import br.com.petshop.system.model.Day;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpdateRequest implements Serializable {
    private Category type;
    private String description;
    private List<Day> days;
}