package br.com.petshop.customer.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerChangePasswordRequest implements Serializable {
    private String oldPassword;
    private String newPassword;
}
