package br.com.petshop.category.service;

import br.com.petshop.category.model.entity.CategoryEntity;
import br.com.petshop.category.repository.CategoryRepository;
import br.com.petshop.category.repository.CategorySpecification;
import br.com.petshop.commons.exception.GenericNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Classe responsável pelos serviços de categorias
 */
@Service
public class CategoryService {
    private Logger log = LoggerFactory.getLogger(CategoryService.class);
    @Autowired private CategoryRepository repository;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private CategorySpecification specification;

    /**
     * Método de criação de categoria.
     * @param entity - entidade de categoria
     * @return - entidade de categoria
     */
    public CategoryEntity save(CategoryEntity entity) {
        return repository.save(entity);
    }

    /**
     * Método de atualização de categoria.
     * @param entity - entidade de categoria
     * @return - entidade de categoria
     */
    public CategoryEntity updateById(CategoryEntity entity) {
        return repository.save(entity);
    }

    /**
     * Método de ativação/destivação de categoria.
     * @param entity - entidade de categoria
     * @param patch - dados de ativação/destivação de categoria
     * @return - entidade de categoria
     * @throws JsonPatchException
     * @throws JsonProcessingException
     */
    public CategoryEntity activate (CategoryEntity entity, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        entity = objectMapper.treeToValue(patched, CategoryEntity.class);

        return repository.save(entity);
    }

    /**
     * Método que recupera a categoria pelo id.
     * @param categoryId
     * @return - entidade de categoria
     */
    public CategoryEntity findById(UUID categoryId) {
        return repository.findById(categoryId)
                .orElseThrow(GenericNotFoundException::new);
    }

    /**
     * Método que recupera a categoria ativas pelo id.
     * @param categoryId
     * @return - entidade de categoria
     */
    public CategoryEntity findByIdAndActiveIsTrue(UUID categoryId) {
        return repository.findById(categoryId)
                .orElseThrow(GenericNotFoundException::new);
    }

    /**
     * Método que retorna todas as categorias através dos filtros: company e active.
     * @param companyId
     * @param active
     * @return - lista de entidade de categoria
     */
    public List<CategoryEntity> findAllByCompanyId(UUID companyId, Boolean active) {
        Specification<CategoryEntity> filters = specification.filter(companyId, active);
        return repository.findAll(filters);
    }
}
