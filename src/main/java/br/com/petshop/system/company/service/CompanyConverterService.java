package br.com.petshop.system.company.service;

import br.com.petshop.system.company.model.dto.request.CompanyUpdateRequest;
import br.com.petshop.system.company.model.dto.response.CompanyResponse;
import br.com.petshop.system.company.model.dto.request.CompanyCreateRequest;
import br.com.petshop.system.company.model.dto.response.CompanySummaryResponse;
import br.com.petshop.system.company.model.dto.response.CompanyTableResponse;
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
        request.setCnpj(request.getCnpj().replaceAll("[^0-9]", ""));
        return mapper.map(request, CompanyEntity.class);
    }

    public CompanyEntity updateRequestIntoEntity(CompanyUpdateRequest request) {
        return mapper.map(request, CompanyEntity.class);
    }

    public CompanyEntity updateRequestIntoEntity(CompanyEntity request, CompanyEntity entity) {
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(request, entity);
//        if (request.getCategories() != null)
//            entity.setCategories(request.getCategories());
        return entity;
    }

    public CompanyResponse entityIntoResponse(CompanyEntity entity) {
        return mapper.map(entity, CompanyResponse.class);
    }

    public CompanyTableResponse entityIntoTableResponse(CompanyEntity entity) {
        return mapper.map(entity, CompanyTableResponse.class);
    }

    public CompanySummaryResponse entityIntoAppResponse(CompanyEntity entity) {
        return mapper.map(entity, CompanySummaryResponse.class);
    }
}
