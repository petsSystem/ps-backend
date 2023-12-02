package br.com.petshop.system.profile.model.dto.response;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.system.profile.model.dto.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse implements Serializable {
    private String id;
    private String name;
    private Role role;
    private List<Permission> permissions;
    private LocalDateTime created;
}
