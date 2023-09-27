package br.com.petshop.system.access.service;

import br.com.petshop.system.access.model.dto.request.AccessGroupCreateRequest;
import br.com.petshop.system.access.model.dto.request.AccessGroupUpdateRequest;
import br.com.petshop.system.access.model.dto.response.AccessGroupResponse;
import br.com.petshop.system.access.model.entity.AccessGroupEntity;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccessConverterService {

    @Autowired
    private ModelMapper mapper;

    public AccessGroupEntity createRequestIntoEntity(AccessGroupCreateRequest request) {
        return mapper.map(request, AccessGroupEntity.class);
    }
    public AccessGroupResponse entityIntoResponse(AccessGroupEntity entity) {
        AccessGroupResponse response = mapper.map(entity, AccessGroupResponse.class);
        return response;

    }
    public AccessGroupEntity updateRequestIntoEntity(AccessGroupUpdateRequest request, AccessGroupEntity entity) {
        AccessGroupEntity newEntity = mapper.map(request, AccessGroupEntity.class);
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(newEntity, entity);
        return entity;
    }
}