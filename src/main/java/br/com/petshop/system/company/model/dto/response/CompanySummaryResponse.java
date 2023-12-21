package br.com.petshop.system.company.model.dto.response;

import br.com.petshop.system.company.model.enums.Category;
import br.com.petshop.system.model.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class CompanySummaryResponse implements Serializable {
    private String id;
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
