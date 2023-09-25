package br.com.petshop.system.subsidiary.repository;

import br.com.petshop.system.subsidiary.model.entity.SubsidiaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubsidiaryRepository extends JpaRepository<SubsidiaryEntity, Integer> {
//    List<SubsidiaryEntity> findByCompanyId(String companyId);
    Optional<SubsidiaryEntity> findByIdAndActiveIsTrue(String subsidiaryId);
}
