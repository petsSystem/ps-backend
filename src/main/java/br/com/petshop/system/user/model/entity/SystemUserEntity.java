package br.com.petshop.system.user.model.entity;

import br.com.petshop.authentication.model.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "system_user")
public class SystemUserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "system_user_id")
    private String id;
    @Column(name = "system_user_email", unique = true)
    private String email;
    @Column(name = "system_user_password")
    private String password;
    @Column(name = "system_user_change_password")
    private Boolean changePassword;
    @Enumerated(EnumType.STRING)
    @Column(name = "system_user_role")
    private Role role;

    @Column(name = "system_user_active")
    private Boolean active;

    @Column(name = "system_user_email_validated")
    private Boolean emailValidated;

    @Column(name = "system_user_email_token")
    private String emailToken;

    @Column(name = "system_user_email_token_time")
    private LocalDateTime emailTokenTime;



    @Column(name = "system_user_created")
    private LocalDateTime created;

    @Column(name = "system_user_updated")
    private LocalDateTime updated;

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
