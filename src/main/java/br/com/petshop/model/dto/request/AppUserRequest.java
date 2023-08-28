package br.com.petshop.model.dto.request;

import br.com.petshop.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserRequest implements Serializable {
    private String name;
    private String cpf;
    private String email;
    private String password;
    private String phone;
    private String dateBirth;
    @Builder.Default
    private Role role = Role.USER;
    private AddressRequest  address;
}
