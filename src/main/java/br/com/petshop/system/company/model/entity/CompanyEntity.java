package br.com.petshop.system.company.model.entity;

import br.com.petshop.system.audit.AuditorBaseEntity;
import br.com.petshop.system.company.model.enums.Category;
import br.com.petshop.system.employee.model.entity.EmployeeEntity;
import br.com.petshop.system.model.Address;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Address address;

    @Column(columnDefinition = "geometry(Point,4326)")
    private Point geom;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<Category> categories;

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
