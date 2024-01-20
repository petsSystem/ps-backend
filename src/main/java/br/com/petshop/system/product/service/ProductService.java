package br.com.petshop.system.product.service;

import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericNotActiveException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.system.product.model.entity.ProductEntity;
import br.com.petshop.system.product.repository.ProductRepository;
import br.com.petshop.system.product.repository.ProductSpecification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {
    Logger log = LoggerFactory.getLogger(ProductService.class);
    @Autowired private ProductRepository repository;
    @Autowired private ProductConverterService convert;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ProductSpecification specification;

    public ProductEntity create(ProductEntity entity) {
        Optional<ProductEntity> company = repository.findByNameAndCategoryId(entity.getName(), entity.getCategoryId());
        if (company.isPresent())
            throw new GenericAlreadyRegisteredException();

        return save(entity);
    }

    public ProductEntity save(ProductEntity entity) {
        return repository.save(entity);
    }

    public ProductEntity activate (UUID categoryId, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        ProductEntity entity = findById(categoryId);

        entity = applyPatch(patch, entity);

        return repository.save(entity);
    }

    public ProductEntity findById(UUID categoryId) {
        return repository.findById(categoryId)
                .orElseThrow(GenericNotFoundException::new);
    }

    public ProductEntity findByIdAndActiveIsTrue(UUID categoryId) {
        ProductEntity category = repository.findById(categoryId)
                .orElseThrow(GenericNotFoundException::new);

        if (!category.getActive())
            throw new GenericNotActiveException();

        return category;
    }

    public ProductEntity updateById(UUID categoryId, ProductEntity request) {
        ProductEntity entity = findByIdAndActiveIsTrue(categoryId);

        entity = convert.updateRequestIntoEntity(request, entity);
        return repository.save(entity);
    }

    private ProductEntity applyPatch(JsonPatch patch, ProductEntity entity) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        return objectMapper.treeToValue(patched, ProductEntity.class);
    }

    public Page<ProductEntity> findAllByCompanyId(UUID companyId, Pageable paging) {
        Specification<ProductEntity> filters = specification.filter(companyId);
        return repository.findAll(filters, paging);
    }
}
