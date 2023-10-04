package br.com.petshop.system.company.model.entity;

import br.com.petshop.system.audit.AuditorBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;

import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_company")
public class CompanyEntity extends AuditorBaseEntity implements Serializable {
    private String name;
    @Column(unique = true)
    private String cnpj;
    private String phone;
    private Boolean active;

    @Column(name = "address_postal_code")
    private String addressPostalCode;
    @Column(name = "address_street")
    private String addressStreet;
    @Column(name = "address_number")
    private String addressNumber;
    @Column(name = "address_neighborhood")
    private String addressNeighborhood;
    @Column(name = "address_city")
    private String addressCity;
    @Column(name = "address_state")
    private String addressState;
    @Column(name = "address_country")
    private String addressCountry;

    @Column(name = "address_lat")
    private Double addressLat;
    @Column(name = "address_lon")
    private Double addressLon;
    @Column(columnDefinition = "geometry(Point,4326)")
    private Point geom;
    private Double distance;
}
