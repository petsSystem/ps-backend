package br.com.petshop.product.service;

import br.com.petshop.commons.exception.GenericAlreadyRegisteredException;
import br.com.petshop.commons.exception.GenericNotFoundException;
import br.com.petshop.product.repository.ProductRepository;
import br.com.petshop.product.model.entity.ProductEntity;
import br.com.petshop.product.repository.ProductSpecification;
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

import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {
    Logger log = LoggerFactory.getLogger(ProductService.class);
    @Autowired private ProductRepository repository;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ProductSpecification specification;

    public ProductEntity create(ProductEntity entity) {
        Optional<ProductEntity> company = repository.findByNameAndCategoryId(entity.getName(), entity.getCategoryId());
        if (company.isPresent())
            throw new GenericAlreadyRegisteredException();

        return repository.save(entity);
    }

    public ProductEntity updateById(ProductEntity entity) {
        return repository.save(entity);
    }

    public ProductEntity activate (ProductEntity entity, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        entity = objectMapper.treeToValue(patched, ProductEntity.class);

        return repository.save(entity);
    }

    public ProductEntity findById(UUID categoryId) {
        return repository.findById(categoryId)
                .orElseThrow(GenericNotFoundException::new);
    }

    public ProductEntity findByIdAndActiveIsTrue(UUID categoryId) {
        return repository.findById(categoryId)
                .orElseThrow(GenericNotFoundException::new);
    }

    public Page<ProductEntity> findAllByCompanyId(UUID companyId, UUID categoryId, Boolean additional, Pageable paging) {
        Specification<ProductEntity> filters = specification.filter(companyId, categoryId, additional);
        return repository.findAll(filters, paging);
    }
}
