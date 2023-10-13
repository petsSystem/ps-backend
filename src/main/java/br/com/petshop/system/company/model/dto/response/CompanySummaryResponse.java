package br.com.petshop.system.company.model.dto.response;

import br.com.petshop.system.model.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

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
