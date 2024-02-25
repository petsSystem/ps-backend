package br.com.petshop.product.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Classe dto responsável pelo retorno dos dados de um produto/serviço adicional.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdditionalResponse implements Serializable {
    private UUID id;
    private String name;
    private BigDecimal amount;
}
