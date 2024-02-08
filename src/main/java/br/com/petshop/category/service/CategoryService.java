package br.com.petshop.category.service;

import br.com.petshop.commons.exception.GenericAlreadyRegisteredException;
import br.com.petshop.commons.exception.GenericNotActiveException;
import br.com.petshop.commons.exception.GenericNotFoundException;
import br.com.petshop.category.model.entity.CategoryEntity;
import br.com.petshop.category.model.enums.Category;
import br.com.petshop.category.repository.CategoryRepository;
import br.com.petshop.category.repository.CategorySpecification;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {
    private Logger log = LoggerFactory.getLogger(CategoryService.class);
    @Autowired private CategoryRepository repository;
    @Autowired private CategoryConverterService convert;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private CategorySpecification specification;

    public CategoryEntity create(CategoryEntity entity) {
        Optional<CategoryEntity> company = repository.findByTypeAndCompanyId(entity.getType(), entity.getCompanyId());
        if (company.isPresent())
            throw new GenericAlreadyRegisteredException();

        return save(entity);
    }

    public void createAutomatic(UUID companyId) {
        Category.stream()
                .forEach(c -> {
                    save(CategoryEntity.builder()
                            .companyId(companyId)
                            .type(c)
                            .description(c.getDescription())
                            .days(new ArrayList<>())
                            .active(false)
                            .build());
                });
    }

    public CategoryEntity save(CategoryEntity entity) {
        return repository.save(entity);
    }

    public CategoryEntity activate (UUID categoryId, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        CategoryEntity entity = findById(categoryId);

        entity = applyPatch(patch, entity);

        return repository.save(entity);
    }

    public CategoryEntity findById(UUID categoryId) {
        return repository.findById(categoryId)
                .orElseThrow(GenericNotFoundException::new);
    }

    public CategoryEntity findByIdAndActiveIsTrue(UUID categoryId) {
        CategoryEntity category = repository.findById(categoryId)
                .orElseThrow(GenericNotFoundException::new);

        if (!category.getActive())
            throw new GenericNotActiveException();

        return category;
    }

    public CategoryEntity updateById(UUID categoryId, CategoryEntity request) {
        CategoryEntity entity = findById(categoryId);

        entity = convert.updateRequestIntoEntity(request, entity);
        return repository.save(entity);
    }

    private CategoryEntity applyPatch(JsonPatch patch, CategoryEntity entity) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        return objectMapper.treeToValue(patched, CategoryEntity.class);
    }

    public List<CategoryEntity> findAllByCompanyId(UUID companyId) {
        Specification<CategoryEntity> filters = specification.filter(companyId);
        return repository.findAll(filters);
    }
}
