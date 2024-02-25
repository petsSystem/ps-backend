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
 * Classe dto responsável pela atualização de um produto/serviço do petshop.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateRequest implements Serializable {
    private String name;
    private BigDecimal amount;
    private Integer intervalMinutes;
    private Boolean additional;
    private List<UUID> additionalIds;
}