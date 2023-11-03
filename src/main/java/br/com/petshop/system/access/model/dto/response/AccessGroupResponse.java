package br.com.petshop.system.access.model.dto.response;

import br.com.petshop.system.access.model.dto.Functionality;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccessGroupResponse implements Serializable {
    private String id;
    private String name;
    private List<Functionality> accesses;
    private LocalDateTime created;
}
