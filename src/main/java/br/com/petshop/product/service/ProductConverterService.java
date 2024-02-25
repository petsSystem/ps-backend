package br.com.petshop.product.service;

import br.com.petshop.product.model.dto.request.ProductCreateRequest;
import br.com.petshop.product.model.dto.request.ProductUpdateRequest;
import br.com.petshop.product.model.dto.response.AdditionalResponse;
import br.com.petshop.product.model.dto.response.ProductResponse;
import br.com.petshop.product.model.dto.response.ProductTableResponse;
import br.com.petshop.product.model.entity.ProductEntity;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Classe responsável pelas conversões de objetos do produto/serviço
 */
@Service
public class ProductConverterService {

    @Autowired private ModelMapper mapper;

    /**
     * Método que converte dto (createRequest) em entidade
     * @param request - dto com dados de criação
     * @return - entidade
     */
    public ProductEntity createRequestIntoEntity(ProductCreateRequest request) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(request, ProductEntity.class);
    }

    /**
     * Método que converte dto (updateRequest) em entidade
     * @param request - dto com dados de atualização
     * @return - entidade
     */
    public ProductEntity updateRequestIntoEntity(ProductUpdateRequest request) {
        return mapper.map(request, ProductEntity.class);
    }

    /**
     * Método que faz merge de duas entidades
     * @param request - entidade com dados de atualização
     * @param entity - entidade atualmente salva em banco
     * @return - entidade
     */
    public ProductEntity updateRequestIntoEntity(ProductEntity request, ProductEntity entity) {
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(request, entity);
        return entity;
    }

    /**
     * Método que converte entidade em dto (response)
     * @param entity - entidade
     * @return - dto de response
     */
    public ProductResponse entityIntoResponse(ProductEntity entity) {
        return mapper.map(entity, ProductResponse.class);
    }

    /**
     * Método que converte entidade em dto (response)
     * @param entity - entidade
     * @return - dto de response
     */
    public ProductTableResponse entityIntoTableResponse(ProductEntity entity) {
        ProductTableResponse response = mapper.map(entity, ProductTableResponse.class);
        response.setAdditionalIds(entity.getAdditionalIds());
        return response;
    }

    /**
     * Método que converte entidade em dto (response)
     * @param entity - entidade
     * @return - dto de response
     */
    public AdditionalResponse entityIntoAdditionalResponse(ProductEntity entity) {
        return mapper.map(entity, AdditionalResponse.class);
    }
}
