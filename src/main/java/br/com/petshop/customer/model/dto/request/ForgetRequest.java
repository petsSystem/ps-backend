package br.com.petshop.customer.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

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
