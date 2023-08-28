package br.com.petshop.model.dto.request;

import lombok.Builder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SigninRequest implements Serializable {
    private String email;
    private String password;
}
