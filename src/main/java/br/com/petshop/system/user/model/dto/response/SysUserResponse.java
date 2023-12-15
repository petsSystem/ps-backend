package br.com.petshop.system.user.model.dto.response;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.system.model.Address;
import br.com.petshop.system.profile.model.dto.Permission;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SysUserResponse implements Serializable {
    private UUID id;
    private String name;
    private String cpf;
    private String email;
    private String phone;
    private Boolean active;
    private Address address;
    private List<UUID> companyIds;
    private List<UUID> profileIds;
    private Role role;
    private String username;
    private Boolean changePassword;
    private List<Permission> permissions;
}
