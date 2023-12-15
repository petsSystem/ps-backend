package br.com.petshop.system.user.model.dto.response;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.system.profile.model.dto.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SysUserMeResponse implements Serializable {
    private UUID id;
    private String name;
    private String username;
    private Boolean changePassword;
    private Role role;
    private List<Permission> permissions;
}
