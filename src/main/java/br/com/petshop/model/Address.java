package br.com.petshop.model;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class Address implements Serializable {
    private String postalCode;
    private String street;
    private String number;
    private String neighborhood;
    private String city;
    private String state;
    private String country;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double lat;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double lon;
//    @Column(columnDefinition = "geometry(Point,4326)")
//    private Point geom;
}
