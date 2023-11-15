package br.com.petshop.system.company.service;

import br.com.petshop.system.company.model.dto.request.CompanyUpdateRequest;
import br.com.petshop.system.company.model.dto.response.CompanyResponse;
import br.com.petshop.system.company.model.dto.request.CompanyCreateRequest;
import br.com.petshop.system.company.model.dto.response.CompanySummaryResponse;
import br.com.petshop.system.company.model.entity.CompanyEntity;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyConverterService {

    @Autowired
    private ModelMapper mapper;

    public CompanyEntity createRequestIntoEntity(CompanyCreateRequest request) {
        return mapper.map(request, CompanyEntity.class);
    }

    public CompanyEntity updateRequestIntoEntity(CompanyUpdateRequest request, CompanyEntity entity) {
        CompanyEntity newEntity = mapper.map(request, CompanyEntity.class);
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(newEntity, entity);
        if (request.getCategories() != null)
            entity.setCategories(request.getCategories());
        return entity;
    }

    public CompanyResponse entityIntoResponse(CompanyEntity entity) {
        return mapper.map(entity, CompanyResponse.class);
    }
    public CompanySummaryResponse entityIntoAppResponse(CompanyEntity entity) {
        return mapper.map(entity, CompanySummaryResponse.class);
    }
}
