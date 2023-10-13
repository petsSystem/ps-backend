package br.com.petshop.system.employee.model.entity;

import br.com.petshop.system.audit.AuditorBaseEntity;
import br.com.petshop.system.company.model.entity.CompanyEntity;
import br.com.petshop.system.employee.model.enums.EmployeeType;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.type.SqlTypes;

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
@Table(name = "sys_employee")
public class EmployeeEntity extends AuditorBaseEntity implements Serializable {
    private String name;
    @Column(unique = true)
    private String cpf;
    @Column(unique = true)
    private String email;
    private String phone;
    private Boolean active;

    private EmployeeType type;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    @Basic(fetch = FetchType.LAZY)
    private Address address;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    @Basic(fetch = FetchType.LAZY)
    List<String> companyIds;

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
