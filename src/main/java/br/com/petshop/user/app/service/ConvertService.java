package br.com.petshop.user.app.service;

import br.com.petshop.pet.model.dto.request.PetCreateRequest;
import br.com.petshop.pet.model.dto.request.PetUpdateRequest;
import br.com.petshop.pet.model.dto.response.PetResponse;
import br.com.petshop.user.app.model.dto.response.AddressResponse;
import br.com.petshop.user.app.model.dto.response.AppUserResponse;
import br.com.petshop.user.app.model.entity.AddressEntity;
import br.com.petshop.user.app.model.entity.AppUserEntity;
import br.com.petshop.pet.model.entity.PetEntity;
import br.com.petshop.user.app.model.dto.request.AddressRequest;
import br.com.petshop.user.app.model.dto.request.AppUserCreateRequest;
import br.com.petshop.user.app.model.dto.request.AppUserUpdateRequest;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ConvertService {

    @Autowired
    private ModelMapper mapper;

    public AppUserEntity convertAppUserCreateRequestIntoEntity(AppUserCreateRequest request) {
        return mapper.map(request, AppUserEntity.class);
    }
    public AppUserResponse convertAppUserEntityIntoResponse(AppUserEntity entity) {
        AppUserResponse response = mapper.map(entity, AppUserResponse.class);
        response.setToken(null);
        if (response.getAddresses() != null) {
            Set<AddressResponse> addressResponses = entity.getAppUserAddresses().stream()
                    .map(a -> convertAddressEntityIntoResponse(a))
                    .collect(Collectors.toSet());
            response.setAddresses(addressResponses);
        }
        if (response.getPets() != null) {
            Set<PetResponse> petResponses = entity.getAppUserPets().stream()
                    .filter(p -> p.getActive())
                    .map(p -> convertPetEntityIntoResponse(p))
                    .collect(Collectors.toSet());
            response.setPets(petResponses);
        }
        return response;

    }
    public AppUserEntity convertAppUserUpdateRequestIntoEntity(AppUserUpdateRequest request, AppUserEntity entity) {
        AppUserEntity newEntity = mapper.map(request, AppUserEntity.class);
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(newEntity, entity);
        return entity;
    }
    public AddressEntity convertAddressRequestIntoEntity(AddressRequest request) {
        return mapper.map(request, AddressEntity.class);
    }

    public AddressResponse convertAddressEntityIntoResponse(AddressEntity entity) {
        return mapper.map(entity, AddressResponse.class);
    }

    public PetEntity convertPetCreateRequestIntoEntity(PetCreateRequest request) {
        return mapper.map(request, PetEntity.class);
    }

    public PetEntity convertPetUpdateRequestIntoEntity(PetUpdateRequest request, PetEntity entity) {
        PetEntity newEntity = mapper.map(request, PetEntity.class);
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(newEntity, entity);
        return entity;
    }

    public PetResponse convertPetEntityIntoResponse(PetEntity entity) {
        return mapper.map(entity, PetResponse.class);
    }
}
