package br.com.petshop.system.poc;

import br.com.petshop.system.audit.AuditorBaseEntity;
import br.com.petshop.system.model.Address;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.locationtech.jts.geom.Point;


import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_poc")
//@Convert(attributeName = "entityAttrName", converter = StringJsonUserType.class)
public class PocEntity extends AuditorBaseEntity implements Serializable {
    private String name;
    @Column(unique = true)
    private String cpf;
    @Column(unique = true)
    private String email;
    private String phone;
    private Boolean active;
    @Column(columnDefinition = "geometry(Point,4326)")
    private Point geom;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    @Basic(fetch = FetchType.LAZY)
    private Address address;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    @Basic(fetch = FetchType.LAZY)
    List<String> companyIds;
}
