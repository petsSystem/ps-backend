package br.com.petshop.system.user.model.dto.response;

import br.com.petshop.authentication.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SysUserResponse implements Serializable {
    private String email;
    private Boolean changePassword;
    private Role role;
    private Boolean active;

}
