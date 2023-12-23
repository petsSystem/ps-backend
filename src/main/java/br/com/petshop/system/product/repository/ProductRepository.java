package br.com.petshop.system.product.repository;

import br.com.petshop.system.product.model.entity.ProductEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Integer> {
    Optional<ProductEntity> findById(UUID categoryId);
    Optional<ProductEntity> findByNameAndCategoryId(String name, UUID categoryId);
    List<ProductEntity> findAll(Specification<ProductEntity> filter);
}
