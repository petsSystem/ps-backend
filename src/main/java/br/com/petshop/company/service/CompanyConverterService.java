package br.com.petshop.company.service;

import br.com.petshop.company.model.dto.response.CompanyResponse;
import br.com.petshop.company.model.dto.response.CompanySummaryResponse;
import br.com.petshop.company.model.entity.CompanyEntity;
import br.com.petshop.company.model.dto.request.CompanyUpdateRequest;
import br.com.petshop.company.model.dto.request.CompanyCreateRequest;
import br.com.petshop.company.model.dto.response.CompanyTableResponse;
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
