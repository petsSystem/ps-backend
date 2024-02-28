package br.com.petshop.company.model.dto.response;

import br.com.petshop.category.model.enums.Category;
import br.com.petshop.commons.model.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Classe dto responsável pelo retorno dos dados de uma loja/petshop.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanySummaryResponse implements Serializable {
    private UUID id;
    private String name;
    private String phone;
    private String formattedAddress;
    private Double distance;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Category> categories;

    @JsonIgnore
    private Address address;

    public String getFormattedAddress() {
        return this.address.getStreet()
                .concat(", ")
                .concat(this.address.getNumber())
                .concat(" - ")
                .concat(this.address.getNeighborhood())
                .concat(", ")
                .concat(this.address.getCity())
                .concat(" - ")
                .concat(this.address.getState())
                .concat(", ")
                .concat(this.address.getPostalCode())
                .concat(", ")
                .concat(this.address.getCountry());
    }
}
