package br.com.petshop.system.user.model.dto.request;

import br.com.petshop.authentication.model.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUserCreateRequest implements Serializable {
    private String email;
    private UUID employeeId;
    private UUID accessGroupId;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Builder.Default
    private Boolean changePassword = false;
    @Builder.Default
    private Boolean active = true;
    private List<UUID> accessGroupIds;
}
