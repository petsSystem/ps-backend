package br.com.petshop.system.employee.model.entity;

import br.com.petshop.system.subsidiary.model.entity.SubsidiaryEntity;
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
@Table(name = "employee")
public class EmployeeEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "employee_id")
    private String id;
    @Column(name = "employee_type")
    private String type;
    @Column(name = "employee_name")
    private String name;
    @Column(name = "employee_cpf", unique = true)
    private String cpf;
    @Column(name = "employee_email", unique = true)
    private String email;
    @Column(name = "employee_phone")
    private String phone;

    @Column(name = "employee_address_postal_code")
    private String addressPostalCode;
    @Column(name = "employee_address_street")
    private String addressStreet;
    @Column(name = "employee_address_number")
    private String addressNumber;
    @Column(name = "employee_address_neighborhood")
    private String addressNeighborhood;
    @Column(name = "employee_address_city")
    private String addressCity;
    @Column(name = "employee_address_state")
    private String addressState;
    @Column(name = "employee_address_country")
    private String addressCountry;
    @Column(name = "employee_address_lat")
    private String addressLat;
    @Column(name = "employee_address_lon")
    private String addressLon;

    @Column(name = "employee_active")
    private Boolean active;

    @Column(name = "employee_creator")
    private String creator;
    @Column(name = "employee_created")
    private LocalDateTime created;
    @Column(name = "employee_updated")
    private LocalDateTime updated;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subsidiary_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private SubsidiaryEntity subsidiary;
}
