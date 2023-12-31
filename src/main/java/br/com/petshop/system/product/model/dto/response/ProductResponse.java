package br.com.petshop.system.product.model.dto.response;

import br.com.petshop.system.model.Day;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse implements Serializable {
    private UUID id;
    private UUID categoryId;
    private String name;
    private BigDecimal amount;
    private List<Day> days;
    private Boolean additional;
    private Integer appointmentConfig;
    private Boolean active;
}
