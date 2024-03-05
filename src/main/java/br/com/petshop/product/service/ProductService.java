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

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Classe responsável pelos serviços de produtos/serviços
 */
@Service
public class ProductService {
    Logger log = LoggerFactory.getLogger(ProductService.class);
    @Autowired private ProductRepository repository;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ProductSpecification specification;

    /**
     * Método de criação de produto/serviço.
     * @param entity - entidade de produto/serviço
     * @return - entidade de produto/serviço
     */
    public ProductEntity create(ProductEntity entity) {
        Optional<ProductEntity> company = repository.findByNameAndCategoryId(entity.getName(), entity.getCategoryId());
        if (company.isPresent())
            throw new GenericAlreadyRegisteredException();

        return repository.save(entity);
    }

    /**
     * Método de atualização de produto/serviço, através de id informado.
     * @param entity - entidade de produto/serviço
     * @return - entidade de produto/serviço
     */
    public ProductEntity updateById(ProductEntity entity) {
        return repository.save(entity);
    }

    /**
     * Método de ativação/desativação de produto/serviço.
     * @param entity - entidade de produto/serviço
     * @param patch - dados de ativação/desativação do produto/serviço
     * @return - entidade de produto/serviço
     * @throws JsonPatchException
     * @throws JsonProcessingException
     */
    public ProductEntity activate (ProductEntity entity, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        entity = objectMapper.treeToValue(patched, ProductEntity.class);

        return repository.save(entity);
    }

    /**
     * Método que recupera dados de produto/serviço, através do id.
     * @param productId - id de cadastro do produto/serviço
     * @return - entidade de produto/serviço
     */
    public ProductEntity findById(UUID productId) {
        return repository.findById(productId)
                .orElseThrow(GenericNotFoundException::new);
    }

    /**
     * Método que recupera dados de produto/serviço ativo, através do id.
     * @param productId - id de cadastro do produto/serviço
     * @return - entidade de produto/serviço
     */
    public ProductEntity findByIdAndActiveIsTrue(UUID productId) {
        return repository.findById(productId)
                .orElseThrow(GenericNotFoundException::new);
    }

    /**
     * Método que recupera dados de produto/serviço, através de filtro informado.
     * @param companyId - id de cadastro da loja/petshop
     * @param categoryId - id de cadastro da categoria
     * @param additional - filtro de pesquisa.
     *                   Se true = buscará todos os produtos/serviços adicionais
     *                   Se false = buscará todos os produtos/serviços principais
     *                   Se null (não informado) = retornará todos os produtos/serviços
     * @param paging - dados de paginação
     * @return - lista de entidades de produto/serviço
     */
    public Page<ProductEntity> findAllByCompanyId(UUID companyId, UUID categoryId, Boolean additional, Pageable paging) {
        Specification<ProductEntity> filters = specification.filter(companyId, categoryId, additional);
        return repository.findAll(filters, paging);
    }

    /**
     * Método que recupera dados de produto/serviço, através de filtro informado.
     * @param companyId - id de cadastro da loja/petshop
     * @param categoryId - id de cadastro da categoria
     * @param additional - filtro de pesquisa.
     *                   Se true = buscará todos os produtos/serviços adicionais
     *                   Se false = buscará todos os produtos/serviços principais
     *                   Se null (não informado) = retornará todos os produtos/serviços
     * @return - lista de entidades de produto/serviço
     */
    public List<ProductEntity> findAll(UUID companyId, UUID categoryId, Boolean additional) {
        Specification<ProductEntity> filters = specification.filter(companyId, categoryId, additional);
        return repository.findAll(filters);
    }
}
