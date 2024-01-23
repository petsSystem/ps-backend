package br.com.petshop.profile.service;

import br.com.petshop.profile.model.dto.request.ProfileCreateRequest;
import br.com.petshop.profile.model.dto.response.LabelResponse;
import br.com.petshop.profile.model.dto.response.ProfileResponse;
import br.com.petshop.profile.model.entity.ProfileEntity;
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
        return mapper.map(entity, ProfileResponse.class);
    }

    public LabelResponse entityIntoLabelResponse(ProfileEntity entity) {
        return mapper.map(entity, LabelResponse.class);
    }
}
