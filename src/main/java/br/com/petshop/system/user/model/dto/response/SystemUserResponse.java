package br.com.petshop.system.user.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SystemUserResponse implements Serializable {
    private String id;
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean changePassword;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Boolean emailValidated;
    private LocalDateTime created;
}
