package br.com.petshop.system.profile.model.dto.request;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.system.profile.model.dto.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileCreateRequest implements Serializable {
    private String name;
    private Role role;
    private List<Permission> permissions;
}
