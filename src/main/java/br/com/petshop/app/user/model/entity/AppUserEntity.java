package br.com.petshop.app.user.model.entity;

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
@Table(name = "app_user")
public class AppUserEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "app_user_id")
    private String id;
    @Column(name = "app_user_name")
    private String name;
    @Column(name = "app_user_cpf")
    private String cpf;
    @Column(name = "app_user_email", unique = true)
    private String email;
    @Column(name = "app_user_password")
    private String password;
    @Column(name = "app_user_change_password")
    private Boolean changePassword;
    @Column(name = "app_user_phone")
    private String phone;
    @Column(name = "app_user_date_birth")
    private String dateBirth;
    @Enumerated(EnumType.STRING)
    @Column(name = "app_user_role")
    private Role role;

    @Column(name = "app_user_active")
    private Boolean active;

    @Column(name = "app_user_email_validated")
    private Boolean emailValidated;

    @Column(name = "app_user_email_token")
    private String emailToken;

    @Column(name = "app_user_email_token_time")
    private LocalDateTime emailTokenTime;

    @Column(name = "app_user_created")
    private LocalDateTime created;

    @Column(name = "app_user_updated")
    private LocalDateTime updated;

//    @ManyToMany(cascade= CascadeType.ALL)
//    @JoinTable(
//            name = "app_user_address",
//            joinColumns = @JoinColumn(name = "app_user_id"),
//            inverseJoinColumns = @JoinColumn(name = "address_id"))
//    Set<AddressEntity> appUserAddresses;
//
//    @ManyToMany(cascade= CascadeType.ALL)
//    @JoinTable(
//            name = "app_user_pet",
//            joinColumns = @JoinColumn(name = "app_user_id"),
//            inverseJoinColumns = @JoinColumn(name = "pet_id"))
//    Set<PetEntity> appUserPets;

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
