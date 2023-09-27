package br.com.petshop.app.user.model.dto.request;

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
public class AppUserUpdateRequest implements Serializable {
    private String name;
    private String cpf;
    private String password;
    private String phone;
    private String dateBirth;
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();
}
