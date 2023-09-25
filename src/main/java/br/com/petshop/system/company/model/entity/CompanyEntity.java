package br.com.petshop.system.company.model.entity;

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
@Table(name = "company")
public class CompanyEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "company_id")
    private String id;
    @Column(name = "company_name")
    private String name;
    @Column(name = "company_cnpj", unique = true)
    private String cnpj;
    @Column(name = "company_active")
    private Boolean active;
    @Column(name = "company_created")
    private LocalDateTime created;
    @Column(name = "company_updated")
    private LocalDateTime updated;
}
