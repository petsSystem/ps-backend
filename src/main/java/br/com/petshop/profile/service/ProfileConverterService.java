package br.com.petshop.profile.service;

import br.com.petshop.product.model.entity.ProductEntity;
import br.com.petshop.profile.model.dto.request.ProfileCreateRequest;
import br.com.petshop.profile.model.dto.request.ProfileUpdateRequest;
import br.com.petshop.profile.model.dto.response.ProfileLabelResponse;
import br.com.petshop.profile.model.dto.response.ProfileResponse;
import br.com.petshop.profile.model.entity.ProfileEntity;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileConverterService {

    @Autowired
    private ModelMapper mapper;

    public ProfileEntity createRequestIntoEntity(ProfileCreateRequest request) {
        return mapper.map(request, ProfileEntity.class);
    }

    public ProfileEntity updateRequestIntoEntity(ProfileUpdateRequest request) {
        return mapper.map(request, ProfileEntity.class);
    }

    public ProfileEntity updateRequestIntoEntity(ProfileEntity request, ProfileEntity entity) {
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(request, entity);
        entity.setPermissions(request.getPermissions());
        return entity;
    }
    public ProfileResponse entityIntoResponse(ProfileEntity entity) {
        return mapper.map(entity, ProfileResponse.class);
    }

    public ProfileLabelResponse entityIntoLabelResponse(ProfileEntity entity) {
        return mapper.map(entity, ProfileLabelResponse.class);
    }
}
