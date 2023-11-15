package br.com.petshop.system.employee.model.entity;

import br.com.petshop.system.audit.AuditorBaseEntity;
import br.com.petshop.system.employee.model.enums.EmployeeType;
import br.com.petshop.system.model.Address;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_employee")
public class EmployeeEntity extends AuditorBaseEntity implements Serializable {
    private String name;
    @Column(unique = true)
    private String cpf;
    private EmployeeType type;
    @Column(unique = true)
    private String email;
    private String phone;
    private Boolean active;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Address address;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "company_ids", columnDefinition = "jsonb")
    List<UUID> companyIds;

//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "company_id", nullable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    @JsonIgnore
//    private CompanyEntity company;
//
//    @ManyToMany(cascade= CascadeType.ALL)
//    @JoinTable(
//            name = "sys_company_employee",
//            joinColumns = @JoinColumn(name = "employee_id", referencedColumnName = "id"),
//            inverseJoinColumns = @JoinColumn(name = "company_id", referencedColumnName = "id"))
//    Set<CompanyEntity> companyEmployees;
}
