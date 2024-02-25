package br.com.petshop.profile.repository;

import br.com.petshop.profile.model.entity.ProfileEntity;
import br.com.petshop.profile.model.dto.response.ProfileLabelResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Classe repositório do perfil do usuário do sistema.
 */
@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Integer> {
    Optional<ProfileEntity> findByName(String name);
    Optional<ProfileEntity> findById(UUID profileId);
    Page<ProfileEntity> findAll(Pageable pageable);
    @Query(value = "SELECT id, name from sys_profile", nativeQuery = true)
    List<ProfileLabelResponse> findAllNames();
}
