package br.com.petshop.system.access.repository;

import br.com.petshop.system.access.model.entity.AccessGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccessGroupRepository extends JpaRepository<AccessGroupEntity, Integer> {
    Optional<AccessGroupEntity> findByName(String name);
    Optional<AccessGroupEntity> findById(String accessGroupId);
}
