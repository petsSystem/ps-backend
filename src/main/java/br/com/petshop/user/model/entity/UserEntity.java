package br.com.petshop.user.model.entity;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.commons.audit.AuditorBaseEntity;
import br.com.petshop.commons.model.Address;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Classe entidade que representa um usuário do sistema.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_user")
public class UserEntity extends AuditorBaseEntity implements UserDetails {
    private String name;
    @Column(unique = true)
    private String cpf;
    private String email;
    private String phone;
    private Boolean active;

    @Column(name = "current_company_id")
    private UUID currentCompanyId;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Address address;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "company_ids", columnDefinition = "jsonb")
    private List<UUID> companyIds;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "profile_ids", columnDefinition = "jsonb")
    private List<UUID> profileIds;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(unique = true)
    private String username;
    private String password;
    @Column(name = "change_password")
    private Boolean changePassword;
    @Column(name = "email_token")
    private String emailToken;
    @Column(name = "email_token_time")
    private LocalDateTime emailTokenTime;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    
}
