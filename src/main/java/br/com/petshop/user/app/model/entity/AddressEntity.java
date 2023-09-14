package br.com.petshop.user.app.model.entity;

import br.com.petshop.user.app.model.entity.AppUserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address")
public class AddressEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "address_id")
    private String id;
    @Column(name = "address_street")
    private String street;
    @Column(name = "address_number")
    private String number;
    @Column(name = "address_postal_code")
    private String postalCode;
    @Column(name = "address_neighborhood")
    private String neighborhood;
    @Column(name = "address_city")
    private String city;
    @Column(name = "address_state")
    private String state;
    @Column(name = "address_country")
    private String country;
    @Column(name = "address_lat")
    private String lat;
    @Column(name = "address_lon")
    private String lon;

    @JsonIgnore
    @ManyToMany(mappedBy = "appUserAddresses")
    Set<AppUserEntity> appUsers;
}
