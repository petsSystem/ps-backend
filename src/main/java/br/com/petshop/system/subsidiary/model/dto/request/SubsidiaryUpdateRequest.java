package br.com.petshop.system.subsidiary.model.dto.request;

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
public class SubsidiaryUpdateRequest implements Serializable {
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

    @Builder.Default
    private LocalDateTime update = LocalDateTime.now();
}
