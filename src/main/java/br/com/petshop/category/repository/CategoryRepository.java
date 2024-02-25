package br.com.petshop.category.repository;

import br.com.petshop.category.model.entity.CategoryEntity;
import br.com.petshop.category.model.enums.Category;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Classe repositório da categoria
 */
@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Integer> {
    Optional<CategoryEntity> findById(UUID categoryId);
    Optional<CategoryEntity> findByTypeAndCompanyId(Category type, UUID companyId);
    List<CategoryEntity> findAll(Specification<CategoryEntity> filter);
}
