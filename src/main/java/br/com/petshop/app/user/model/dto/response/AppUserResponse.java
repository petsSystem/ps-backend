package br.com.petshop.app.user.model.dto.response;

import br.com.petshop.app.address.model.dto.response.AppAddressResponse;
import br.com.petshop.app.pet.model.dto.response.PetResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUserResponse implements Serializable {
    private String id;
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
    private Set<AppAddressResponse> addresses;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Set<PetResponse> pets;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean emailValidated;
    private LocalDateTime createdAt;
}
