package br.com.petshop.category.service;

import br.com.petshop.category.model.dto.request.CategoryUpdateRequest;
import br.com.petshop.category.model.dto.response.CategoryResponse;
import br.com.petshop.category.model.entity.CategoryEntity;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryConverterService {

    @Autowired
    private ModelMapper mapper;

    public CategoryEntity updateRequestIntoEntity(CategoryUpdateRequest request) {
        CategoryEntity entity = mapper.map(request, CategoryEntity.class);
        return entity;
    }

    public CategoryEntity updateRequestIntoEntity(CategoryEntity request, CategoryEntity entity) {
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(request, entity);
        return entity;
    }

    public CategoryResponse entityIntoResponse(CategoryEntity entity) {
        return mapper.map(entity, CategoryResponse.class);
    }
}
