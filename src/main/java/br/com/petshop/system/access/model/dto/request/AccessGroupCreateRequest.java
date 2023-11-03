package br.com.petshop.system.access.model.dto.request;

import br.com.petshop.system.access.model.dto.Permission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessGroupCreateRequest implements Serializable {
    private String name;
    private List<Permission> permissions;
}
