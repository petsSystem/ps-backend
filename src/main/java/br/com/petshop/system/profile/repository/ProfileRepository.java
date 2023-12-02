package br.com.petshop.system.profile.repository;

import br.com.petshop.system.profile.model.entity.ProfileEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Integer> {
    Optional<ProfileEntity> findByName(String name);
    Optional<ProfileEntity> findById(UUID profileId);
    Page<ProfileEntity> findAll(Pageable pageable);
    @Query(value = "SELECT name from sys_profile", nativeQuery = true)
    List<String> findAllNames();
}
