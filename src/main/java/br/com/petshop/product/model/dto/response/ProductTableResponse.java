package br.com.petshop.product.model.dto.response;

import br.com.petshop.category.model.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductTableResponse implements Serializable {
    private UUID id;
    private String name;
    private Category category;
    private String categoryLabel;
    private BigDecimal amount;
    private Boolean active;

    public String getCategoryLabel() {
        return this.category.getLabel();
    }
}
