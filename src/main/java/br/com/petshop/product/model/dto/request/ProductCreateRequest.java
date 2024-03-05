package br.com.petshop.product.model.dto.request;

import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private UUID companyId;
    @NotNull
    private UUID categoryId;
    @NotNull
    private String name;
    @NotNull
    private Integer intervalMinutes;
    private BigDecimal amount;
    private Boolean additional;
    private List<UUID> additionalIds;
    @Builder.Default
    private Boolean active = true;
}
