package br.com.petshop.system.employee.model.entity;

import br.com.petshop.system.audit.AuditorBaseEntity;
import br.com.petshop.system.company.model.entity.CompanyEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_employee")
public class EmployeeEntity extends AuditorBaseEntity implements Serializable {
    private String type;
    private String name;
    @Column(unique = true)
    private String cpf;
    @Column(unique = true)
    private String email;
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
    private String addressLat;
    @Column(name = "address_lon")
    private String addressLon;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private CompanyEntity company;

}
