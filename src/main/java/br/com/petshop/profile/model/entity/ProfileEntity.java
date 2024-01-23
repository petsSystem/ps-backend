package br.com.petshop.profile.model.entity;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.profile.model.dto.Permission;
import br.com.petshop.audit.AuditorBaseEntity;
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

import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "profile")
public class ProfileEntity extends AuditorBaseEntity implements Serializable {
    @Column(unique = true)
    private String name;
    private Role role;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private List<Permission> permissions;
}
