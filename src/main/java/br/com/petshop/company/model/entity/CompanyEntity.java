package br.com.petshop.company.model.entity;

import br.com.petshop.commons.audit.AuditorBaseEntity;
import br.com.petshop.commons.model.Address;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;

import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "company")
public class CompanyEntity extends AuditorBaseEntity implements Serializable {
    private String name;
    @Column(unique = true)
    private String cnpj;
    private String phone;
    private Boolean active;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Address address;

    @Column(columnDefinition = "geometry(Point,4326)")
    private Point geom;

//    @JdbcTypeCode(SqlTypes.JSON)
//    @Column(columnDefinition = "jsonb")
//    private List<Category> categories;
//
//    @JdbcTypeCode(SqlTypes.JSON)
//    @Column(columnDefinition = "jsonb", name = "schedule_ids")
//    private List<UUID> scheduleIds;

//
//
//    @JsonIgnore
//    @ManyToMany(mappedBy = "companyEmployees", cascade = CascadeType.ALL)
//    Set<EmployeeEntity> employees;

    //    @ManyToMany(cascade= CascadeType.ALL)
//    @JoinTable(
//            name = "app_user_address",
//            joinColumns = @JoinColumn(name = "app_user_id"),
//            inverseJoinColumns = @JoinColumn(name = "address_id"))
//    Set<AddressEntity> appUserAddresses;
}
