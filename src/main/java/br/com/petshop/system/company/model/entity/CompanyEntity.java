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
    private Boolean active;
}
