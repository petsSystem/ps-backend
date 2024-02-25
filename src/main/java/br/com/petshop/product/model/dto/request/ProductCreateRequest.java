package br.com.petshop.product.model.dto.request;

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
 * Classe dto responsável pela criação de um produto/serviço do petshop.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequest implements Serializable {
    private UUID companyId;
    private UUID categoryId;
    private String name;
    private Integer intervalMinutes;
    private BigDecimal amount;
    private Boolean additional;
    private List<UUID> additionalIds;
    @Builder.Default
    private Boolean active = true;
}
