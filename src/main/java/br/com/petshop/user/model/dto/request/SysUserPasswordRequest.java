package br.com.petshop.user.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Classe dto responsável pela troca de senha de um usuário do sistema.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUserPasswordRequest implements Serializable {
    private String oldPassword;
    private String newPassword;
}
