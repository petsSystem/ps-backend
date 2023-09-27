package br.com.petshop.app.address.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class AppAddressResponse implements Serializable {
    private String id;
    private String postalCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String street;
    private String number;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String neighborhood;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String city;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String state;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String country;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lat;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String lon;
    private LocalDateTime createdAt;
}

