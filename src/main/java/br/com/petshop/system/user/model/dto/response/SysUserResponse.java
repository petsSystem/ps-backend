package br.com.petshop.system.user.model.dto.response;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.system.profile.model.dto.Permission;
import br.com.petshop.system.employee.model.dto.response.EmployeeResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class SysUserResponse implements Serializable {
    private UUID id;
    private String username;
    private Boolean changePassword;
    private Role role;
    private Boolean active;
    private EmployeeResponse employee;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<UUID> accessGroupIds;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<UUID> profiles;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Permission> permissions;
}
