package br.com.petshop.user.service;

import br.com.petshop.user.model.dto.request.SysUserCreateRequest;
import br.com.petshop.user.model.dto.request.SysUserUpdateProfileRequest;
import br.com.petshop.user.model.dto.request.SysUserUpdateRequest;
import br.com.petshop.user.model.dto.response.SysUserMeResponse;
import br.com.petshop.user.model.dto.response.SysUserProfileResponse;
import br.com.petshop.user.model.dto.response.SysUserResponse;
import br.com.petshop.user.model.dto.response.SysUserTableResponse;
import br.com.petshop.user.model.entity.UserEntity;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserConverterService {

    @Autowired
    private ModelMapper mapper;

    public UserEntity createRequestIntoEntity(SysUserCreateRequest request) {
        request.setCpf(request.getCpf().replaceAll("[^0-9]", ""));
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity entity = mapper.map(request, UserEntity.class);
        entity.setCompanyIds(request.getCompanyIds());
        entity.setProfileIds(request.getProfileIds());
        entity.setProductIds(request.getProductIds());

        return entity;
    }

    public UserEntity updateRequestIntoEntity(SysUserUpdateRequest request) {
        return mapper.map(request, UserEntity.class);
    }

    public UserEntity updateRequestIntoEntity(UserEntity request, UserEntity entity) {
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(request, entity);
        entity.setCompanyIds(request.getCompanyIds());
        entity.setProfileIds(request.getProfileIds());
        entity.setProductIds(request.getProductIds());
        return entity;
    }

    public UserEntity updateProfileRequestIntoEntity(SysUserUpdateProfileRequest request) {
        return mapper.map(request, UserEntity.class);
    }

    public UserEntity updateProfileRequestIntoEntity(UserEntity request, UserEntity entity) {
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(request, entity);
        return entity;
    }

    public SysUserResponse entityIntoResponse(UserEntity entity) {
        return mapper.map(entity, SysUserResponse.class);
    }

    public SysUserTableResponse entityIntoTableResponse(UserEntity entity) {
        return mapper.map(entity, SysUserTableResponse.class);
    }

    public SysUserProfileResponse entityIntoProfileResponse(UserEntity entity) {
        return mapper.map(entity, SysUserProfileResponse.class);
    }

    public SysUserMeResponse entityIntoMeResponse(UserEntity entity) {
        return mapper.map(entity, SysUserMeResponse.class);
    }
}
