package br.com.petshop.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {
    private String street;
    private String number;
    private String postalCode;
    private String neighborhood;
    private String city;
    private String state;
    private String country;
    private String lat;
    private String lon;
}
