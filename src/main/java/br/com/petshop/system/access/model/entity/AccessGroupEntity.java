package br.com.petshop.system.access.model.entity;

import br.com.petshop.system.access.model.pojo.Access;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "access_group")
public class AccessGroupEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "access_group_id")
    private String id;
    private String name;
    //    @Type(type = "jsonb")
    @Column(name = "access_group_functionalities", columnDefinition = "jsonb")
    private Access accesses;
    @Column(name = "access_group_created")
    private LocalDateTime created;

    @Column(name = "access_group_updated")
    private LocalDateTime updated;
}
