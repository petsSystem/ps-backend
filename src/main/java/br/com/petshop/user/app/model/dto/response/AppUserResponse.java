package br.com.petshop.user.app.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserResponse implements Serializable {
    private String name;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String cpf;
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String phone;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String dateBirth;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean changePassword;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<AddressResponse> addresses;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean emailValidated;
}
