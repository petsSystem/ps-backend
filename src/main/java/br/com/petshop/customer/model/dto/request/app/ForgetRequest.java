package br.com.petshop.customer.model.dto.request.app;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Classe dto responsável pelo envio de email com nova senha, no caso de esquecimento da mesma.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ForgetRequest implements Serializable {
    private String email;
    @Builder.Default
    private Boolean changePassword = true;
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
