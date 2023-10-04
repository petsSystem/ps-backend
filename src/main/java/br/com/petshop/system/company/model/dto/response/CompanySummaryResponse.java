package br.com.petshop.system.company.model.dto.response;

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
    private String addressPostalCode;
    @JsonIgnore
    private String addressStreet;
    @JsonIgnore
    private String addressNumber;
    @JsonIgnore
    private String addressNeighborhood;
    @JsonIgnore
    private String addressCity;
    @JsonIgnore
    private String addressState;
    @JsonIgnore
    private String addressCountry;

    public String getFormattedAddress() {
        return this.addressStreet
                .concat(", ")
                .concat(this.addressNumber)
                .concat(" - ")
                .concat(this.addressNeighborhood)
                .concat(", ")
                .concat(this.addressCity)
                .concat(" - ")
                .concat(this.addressState)
                .concat(", ")
                .concat(this.addressPostalCode)
                .concat(", ")
                .concat(this.addressCountry);
    }
}
