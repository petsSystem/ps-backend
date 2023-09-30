package br.com.petshop.system.company.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyUpdateRequest implements Serializable {
    private String name;
    private String phone;

    private String addressPostalCode;
    private String addressStreet;
    private String addressNumber;
    private String addressNeighborhood;
    private String addressCity;
    private String addressState;
    private String addressCountry;
    private String addressLat;
    private String addressLon;
}
