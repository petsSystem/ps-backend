package br.com.petshop.system.category.service;

import br.com.petshop.system.category.model.dto.request.CategoryCreateRequest;
import br.com.petshop.system.category.model.dto.request.CategoryUpdateRequest;
import br.com.petshop.system.category.model.dto.response.CategoryResponse;
import br.com.petshop.system.category.model.dto.response.CategoryTableResponse;
import br.com.petshop.system.category.model.entity.CategoryEntity;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryConverterService {

    @Autowired
    private ModelMapper mapper;

    public CategoryEntity createRequestIntoEntity(CategoryCreateRequest request) {
        return mapper.map(request, CategoryEntity.class);
    }

    public CategoryEntity updateRequestIntoEntity(CategoryUpdateRequest request) {
        return mapper.map(request, CategoryEntity.class);
    }

    public CategoryEntity updateRequestIntoEntity(CategoryEntity request, CategoryEntity entity) {
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(request, entity);
        return entity;
    }

    public CategoryResponse entityIntoResponse(CategoryEntity entity) {
        return mapper.map(entity, CategoryResponse.class);
    }

    public CategoryTableResponse entityIntoTableResponse(CategoryEntity entity) {
        return mapper.map(entity, CategoryTableResponse.class);
    }
}
