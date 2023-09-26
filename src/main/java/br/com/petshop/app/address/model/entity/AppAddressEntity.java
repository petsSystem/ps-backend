package br.com.petshop.app.address.model.entity;

import br.com.petshop.app.user.model.entity.AppUserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_address")
public class AppAddressEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "app_address_id")
    private String addressId;
    @Column(name = "app_address_street")
    private String street;
    @Column(name = "app_address_number")
    private String number;
    @Column(name = "app_address_postal_code")
    private String postalCode;
    @Column(name = "app_address_neighborhood")
    private String neighborhood;
    @Column(name = "app_address_city")
    private String city;
    @Column(name = "app_address_state")
    private String state;
    @Column(name = "app_address_country")
    private String country;
    @Column(name = "app_address_lat")
    private String lat;
    @Column(name = "app_address_lon")
    private String lon;
    @Column(name = "app_address_created")
    private LocalDateTime created;
    @Column(name = "app_address_updated")
    private LocalDateTime updated;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "app_user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private AppUserEntity appUser;

//    @JsonIgnore
//    @ManyToMany(mappedBy = "appUserAddresses")
//    Set<AppUserEntity> appUsers;
}
