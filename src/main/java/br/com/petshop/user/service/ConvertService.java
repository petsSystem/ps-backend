package br.com.petshop.user.service;

import br.com.petshop.dao.entity.AddressEntity;
import br.com.petshop.dao.entity.AppUserEntity;
import br.com.petshop.dao.entity.PetEntity;
import br.com.petshop.model.dto.request.AddressRequest;
import br.com.petshop.model.dto.request.AppUserCreateRequest;
import br.com.petshop.model.dto.request.AppUserUpdateRequest;
import br.com.petshop.model.dto.request.PetRequest;
import br.com.petshop.model.dto.response.AddressResponse;
import br.com.petshop.model.dto.response.AppUserResponse;
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

    public PetEntity convertPetRequestIntoEntity(PetRequest request) {
        return mapper.map(request, PetEntity.class);
    }

}
