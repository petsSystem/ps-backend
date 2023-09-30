package br.com.petshop.app.address.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppAddressCreateRequest implements Serializable {
    private String postalCode;
    private String street;
    private String number;
    private String neighborhood;
    private String city;
    private String state;
    private String country;
    private String lat;
    private String lon;
    @Builder.Default
    private Boolean principal = true;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
