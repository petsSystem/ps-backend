package br.com.petshop.product.model.dto.response;

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
public class ProductResponse implements Serializable {
    private UUID id;
    private UUID companyId;
    private UUID categoryId;
    private String name;
    private Integer intervalMinutes;
    private BigDecimal amount;
    private Boolean additional;
    private List<UUID> additionalIds;
    private Boolean active;
}
