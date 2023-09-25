package br.com.petshop.system.subsidiary.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Entity
@Table(name = "subsidiary")
public class SubsidiaryEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "subsidiary_id")
    private String id;
    @Column(name = "subsidiary_name")
    private String name;
    @Column(name = "subsidiary_phone")
    private String phone;

    @Column(name = "subsidiary_address_postal_code")
    private String addressPostalCode;
    @Column(name = "subsidiary_address_street")
    private String addressStreet;
    @Column(name = "subsidiary_address_number")
    private String addressNumber;
    @Column(name = "subsidiary_address_neighborhood")
    private String addressNeighborhood;
    @Column(name = "subsidiary_address_city")
    private String addressCity;
    @Column(name = "subsidiary_address_state")
    private String addressState;
    @Column(name = "subsidiary_address_country")
    private String addressCountry;
    @Column(name = "subsidiary_address_lat")
    private String addressLat;
    @Column(name = "subsidiary_address_lon")
    private String addressLon;

    @Column(name = "subsidiary_active")
    private Boolean active;

    @Column(name = "subsidiary_created")
    private LocalDateTime created;
    @Column(name = "subsidiary_updated")
    private LocalDateTime updated;
}
