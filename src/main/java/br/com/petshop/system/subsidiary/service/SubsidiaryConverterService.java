package br.com.petshop.system.subsidiary.service;

import br.com.petshop.system.subsidiary.model.dto.request.SubsidiaryCreateRequest;
import br.com.petshop.system.subsidiary.model.dto.request.SubsidiaryUpdateRequest;
import br.com.petshop.system.subsidiary.model.dto.response.SubsidiaryResponse;
import br.com.petshop.system.subsidiary.model.entity.SubsidiaryEntity;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubsidiaryConverterService {

    @Autowired
    private ModelMapper mapper;

    public SubsidiaryEntity createRequestIntoEntity(SubsidiaryCreateRequest request) {
        return mapper.map(request, SubsidiaryEntity.class);
    }

    public SubsidiaryEntity updateRequestIntoEntity(SubsidiaryUpdateRequest request, SubsidiaryEntity entity) {
        SubsidiaryEntity newEntity = mapper.map(request, SubsidiaryEntity.class);
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(newEntity, entity);
        return entity;
    }

    public SubsidiaryResponse entityIntoResponse(SubsidiaryEntity entity) {
        return mapper.map(entity, SubsidiaryResponse.class);
    }
}
