package br.com.petshop.category.service;

import br.com.petshop.category.model.dto.request.CategoryUpdateRequest;
import br.com.petshop.category.model.dto.response.CategoryResponse;
import br.com.petshop.category.model.entity.CategoryEntity;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Classe responsável pelas conversões de objetos da categoria
 */
@Service
public class CategoryConverterService {

    @Autowired private ModelMapper mapper;

    /**
     * Método que converte dto (updateRequest) em entidade
     * @param request - dto com dados de atualização
     * @return - entidade
     */
    public CategoryEntity updateRequestIntoEntity(CategoryUpdateRequest request) {
        CategoryEntity entity = mapper.map(request, CategoryEntity.class);
        return entity;
    }

    /**
     * Método que faz merge de duas entidades
     * @param request - entidade com dados de atualização
     * @param entity - entidade atualmente salva em banco
     * @return - entidade
     */
    public CategoryEntity updateRequestIntoEntity(CategoryEntity request, CategoryEntity entity) {
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(request, entity);
        return entity;
    }

    /**
     * Método que converte entidade em dto (response)
     * @param entity - entidade
     * @return - dto de response
     */
    public CategoryResponse entityIntoResponse(CategoryEntity entity) {
        return mapper.map(entity, CategoryResponse.class);
    }
}
