package br.com.petshop.system.profile.service;

import br.com.petshop.system.profile.model.dto.request.ProfileCreateRequest;
import br.com.petshop.system.profile.model.dto.response.ProfileResponse;
import br.com.petshop.system.profile.model.entity.ProfileEntity;
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
    public ProfileResponse entityIntoResponse(ProfileEntity entity) {
        ProfileResponse response = mapper.map(entity, ProfileResponse.class);
        return response;
    }
}
