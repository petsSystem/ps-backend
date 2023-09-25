package br.com.petshop.system.access.model.dto.request;

import br.com.petshop.system.access.model.pojo.Access;
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
public class AccessGroupCreateRequest implements Serializable {
    private String name;
    private Access accesses;
    @Builder.Default
    private LocalDateTime created = LocalDateTime.now();
}
