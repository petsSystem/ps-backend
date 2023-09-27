package br.com.petshop.system.company.repository;

import br.com.petshop.system.company.model.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Integer> {
    Optional<CompanyEntity> findById(String companyId);
    Optional<CompanyEntity> findByIdAndActiveIsTrue(String companyId);
    Optional<CompanyEntity> findByCnpjAndActiveIsTrue(String companyId);
}