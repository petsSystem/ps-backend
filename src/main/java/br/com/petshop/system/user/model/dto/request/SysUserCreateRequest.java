package br.com.petshop.system.user.model.dto.request;

import br.com.petshop.authentication.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUserCreateRequest implements Serializable {
    private String email;
    private String password;
    @Builder.Default
    private Boolean changePassword = false;
    @Builder.Default
    private Role role = Role.USER;
    @Builder.Default
    private Boolean active = true;
    @Builder.Default
    private Boolean emailValidated = false;
//    @Builder.Default
//    private LocalDateTime created = LocalDateTime.now();
}
