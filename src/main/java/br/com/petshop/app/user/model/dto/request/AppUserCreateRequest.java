package br.com.petshop.app.user.model.dto.request;

import br.com.petshop.authentication.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserCreateRequest implements Serializable {
    private String name;
    private String email;
    private String password;
    @Builder.Default
    private Role role = Role.USER;
    @Builder.Default
    private Boolean changePassword = false;
    @Builder.Default
    private Boolean active = true;
    @Builder.Default
    private Boolean emailValidated = false;
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
}
