package br.com.petshop.pet.service;

import br.com.petshop.pet.model.dto.request.PetCreateRequest;
import br.com.petshop.pet.model.dto.request.PetUpdateRequest;
import br.com.petshop.pet.model.dto.response.PetResponse;
import br.com.petshop.pet.model.entity.PetEntity;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PetConverterService {

    @Autowired
    private ModelMapper mapper;

    public PetEntity createRequestIntoEntity(PetCreateRequest request) {
        return mapper.map(request, PetEntity.class);
    }

    public PetEntity updateRequestIntoEntity(PetUpdateRequest request) {
        return mapper.map(request, PetEntity.class);
    }

    public PetEntity updateRequestIntoEntity(PetEntity request, PetEntity entity) {
        PetEntity newEntity = mapper.map(request, PetEntity.class);
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(newEntity, entity);
        return entity;
    }

    public PetResponse entityIntoResponse(PetEntity entity) {
        return mapper.map(entity, PetResponse.class);
    }
}
