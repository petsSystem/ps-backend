package br.com.petshop.product.repository;

import br.com.petshop.product.model.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Classe repositório do produto/serviço
 */
@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    Optional<ProductEntity> findById(UUID categoryId);
    Optional<ProductEntity> findByNameAndCategoryId(String name, UUID categoryId);
    Page<ProductEntity> findAll(Specification<ProductEntity> filter, Pageable paging);
    List<ProductEntity> findAll(Specification<ProductEntity> filter);
}
