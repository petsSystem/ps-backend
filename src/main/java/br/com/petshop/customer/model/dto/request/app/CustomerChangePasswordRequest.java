package br.com.petshop.customer.model.dto.request.app;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Classe dto responsável pela alteração de senha de um cliente pelo aplicativo.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerChangePasswordRequest implements Serializable {
    private String oldPassword;
    private String newPassword;
}
