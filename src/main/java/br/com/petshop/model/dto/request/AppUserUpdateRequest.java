package br.com.petshop.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserUpdateRequest implements Serializable {
    private String name;
    private String cpf;
    private String phone;
    private String dateBirth;
}
