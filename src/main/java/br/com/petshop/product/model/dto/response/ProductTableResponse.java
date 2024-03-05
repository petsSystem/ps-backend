package br.com.petshop.product.model.dto.response;

import br.com.petshop.category.model.enums.Category;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * Classe dto responsável pelo retorno dos dados de um produto/serviço.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductTableResponse implements Serializable {
    private UUID id;
    private String name;
    private UUID categoryId;
    private Category category;
    private String categoryLabel;
    private BigDecimal amount;
    private Boolean additional;
    private List<UUID> additionalIds;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<AdditionalResponse> additionals;
    private Boolean active;

    public String getCategoryLabel() {
        return this.category.getLabel();
    }
}
