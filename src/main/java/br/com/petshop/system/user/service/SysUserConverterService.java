package br.com.petshop.system.user.service;

import br.com.petshop.system.user.model.dto.request.SysUserCreateRequest;
import br.com.petshop.system.user.model.dto.request.SysUserUpdateRequest;
import br.com.petshop.system.user.model.dto.response.SysUserMeResponse;
import br.com.petshop.system.user.model.dto.response.SysUserResponse;
import br.com.petshop.system.user.model.dto.response.SysUserTableResponse;
import br.com.petshop.system.user.model.entity.SysUserEntity;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserConverterService {

    @Autowired
    private ModelMapper mapper;

    public SysUserEntity createRequestIntoEntity(SysUserCreateRequest request) {
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        SysUserEntity entity = mapper.map(request, SysUserEntity.class);
        entity.setCompanyIds(request.getCompanyIds());
        entity.setProfileIds(request.getProfileIds());

        return entity;
    }

    public SysUserEntity updateRequestIntoEntity(SysUserUpdateRequest request) {
        return mapper.map(request, SysUserEntity.class);
    }

    public SysUserEntity updateRequestIntoEntity(SysUserEntity request, SysUserEntity entity) {
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(request, entity);
        return entity;
    }

    public SysUserResponse entityIntoResponse(SysUserEntity entity) {
        return mapper.map(entity, SysUserResponse.class);
    }

    public SysUserTableResponse entityIntoTableResponse(SysUserEntity entity) {
        return mapper.map(entity, SysUserTableResponse.class);
    }

    public SysUserMeResponse entityIntoMeResponse(SysUserEntity entity) {
        return mapper.map(entity, SysUserMeResponse.class);
    }
}
