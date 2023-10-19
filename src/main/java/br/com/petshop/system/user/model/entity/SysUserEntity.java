package br.com.petshop.system.user.model.entity;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.system.audit.AuditorBaseEntity;
import br.com.petshop.system.employee.model.entity.EmployeeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_user")
public class SysUserEntity extends AuditorBaseEntity implements UserDetails {
    @Column(unique = true)
    private String email;
    private String password;
    @Column(name = "change_password")
    private Boolean changePassword;
    @Enumerated(EnumType.STRING)
    private Role role;
    private Boolean active;
    @Column(name = "email_validated")
    private Boolean emailValidated;
    @Column(name = "email_token")
    private String emailToken;
    @Column(name = "email_token_time")
    private LocalDateTime emailTokenTime;
//    private Boolean enabled;
//    private String username;
//    public Boolean accountNonExpired;
//    public Boolean accountNonLocked;
//    public Boolean credentialsNonExpired;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id", referencedColumnName = "id", nullable = true)
    private EmployeeEntity employee;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        // email in our case
        return email;
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