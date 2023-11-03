package br.com.petshop.system.access.repository;

import br.com.petshop.system.access.model.entity.AccessGroupEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccessGroupRepository extends JpaRepository<AccessGroupEntity, Integer> {
    Optional<AccessGroupEntity> findByName(String name);
    Optional<AccessGroupEntity> findById(UUID accessGroupId);

    Page<AccessGroupEntity> findAll(Pageable pageable);
}
