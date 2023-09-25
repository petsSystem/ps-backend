package br.com.petshop.system.user.service;

import br.com.petshop.system.user.model.dto.request.SystemUserCreateRequest;
import br.com.petshop.system.user.model.entity.SystemUserEntity;
import br.com.petshop.system.user.model.dto.request.SystemUserUpdateRequest;
import br.com.petshop.system.user.model.dto.response.SystemUserResponse;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemUserConverterService {

    @Autowired
    private ModelMapper mapper;

    public SystemUserEntity createRequestIntoEntity(SystemUserCreateRequest request) {
        return mapper.map(request, SystemUserEntity.class);
    }
    public SystemUserResponse entityIntoResponse(SystemUserEntity entity) {
        SystemUserResponse response = mapper.map(entity, SystemUserResponse.class);
        response.setToken(null);
        return response;

    }
    public SystemUserEntity updateRequestIntoEntity(SystemUserUpdateRequest request, SystemUserEntity entity) {
        SystemUserEntity newEntity = mapper.map(request, SystemUserEntity.class);
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(newEntity, entity);
        return entity;
    }
}
