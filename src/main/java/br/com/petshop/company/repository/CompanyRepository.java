package br.com.petshop.company.repository;

import br.com.petshop.company.model.entity.CompanyEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Classe repositório da loja/petshop
 */
@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, Integer> {
    Optional<CompanyEntity> findById(UUID companyId);
    Optional<CompanyEntity> findByIdAndActiveIsTrue(UUID companyId);
    Optional<CompanyEntity> findByCnpj(String cnpj);
    Page<CompanyEntity> findAll(Specification<CompanyEntity> filter, Pageable paging);
}
