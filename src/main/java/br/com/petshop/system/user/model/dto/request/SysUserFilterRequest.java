package br.com.petshop.system.user.model.dto.request;

import br.com.petshop.authentication.model.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class    SysUserFilterRequest implements Serializable {
    private String email;
    private UUID employeeId;
    private UUID accessGroupId;
    private Role role;
    private Boolean active;
}
