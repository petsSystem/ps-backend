package br.com.petshop.system.product.service;

import br.com.petshop.system.product.model.dto.request.ProductCreateRequest;
import br.com.petshop.system.product.model.dto.request.ProductUpdateRequest;
import br.com.petshop.system.product.model.dto.response.ProductResponse;
import br.com.petshop.system.product.model.dto.response.ProductTableResponse;
import br.com.petshop.system.product.model.entity.ProductEntity;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductConverterService {

    @Autowired
    private ModelMapper mapper;

    public ProductEntity createRequestIntoEntity(ProductCreateRequest request) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return mapper.map(request, ProductEntity.class);
    }

    public ProductEntity updateRequestIntoEntity(ProductUpdateRequest request) {
        return mapper.map(request, ProductEntity.class);
    }

    public ProductEntity updateRequestIntoEntity(ProductEntity request, ProductEntity entity) {
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(request, entity);
        entity.setDays(request.getDays());
        return entity;
    }

    public ProductResponse entityIntoResponse(ProductEntity entity) {
        return mapper.map(entity, ProductResponse.class);
    }

    public ProductTableResponse entityIntoTableResponse(ProductEntity entity) {
        return mapper.map(entity, ProductTableResponse.class);
    }
}
