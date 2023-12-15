package br.com.petshop.system.user.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SysUserTableResponse implements Serializable {
    private UUID id;
    private String name;
    private String cpf;
    private String email;
    private Boolean active;
}
