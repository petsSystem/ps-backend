package br.com.petshop.app.user.service;

import br.com.petshop.app.user.model.dto.request.AppUserCreateRequest;
import br.com.petshop.app.user.model.dto.request.AppUserUpdateRequest;
import br.com.petshop.app.user.model.dto.response.AppUserResponse;
import br.com.petshop.app.user.model.entity.AppUserEntity;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppUserConverterService {

    @Autowired
    private ModelMapper mapper;

    public AppUserEntity appUserCreateRequestIntoEntity(AppUserCreateRequest request) {
        return mapper.map(request, AppUserEntity.class);
    }
    public AppUserResponse appUserEntityIntoResponse(AppUserEntity entity) {
        AppUserResponse response = mapper.map(entity, AppUserResponse.class);
        response.setToken(null);
//        if (response.getAddresses() != null) {
//            Set<AddressResponse> addressResponses = entity.getAppUserAddresses().stream()
//                    .map(a -> convertAddressEntityIntoResponse(a))
//                    .collect(Collectors.toSet());
//            response.setAddresses(addressResponses);
//        }
//        if (response.getPets() != null) {
//            Set<PetResponse> petResponses = entity.getAppUserPets().stream()
//                    .filter(p -> p.getActive())
//                    .map(p -> convertPetEntityIntoResponse(p))
//                    .collect(Collectors.toSet());
//            response.setPets(petResponses);
//        }
        return response;

    }
    public AppUserEntity appUserUpdateRequestIntoEntity(AppUserUpdateRequest request, AppUserEntity entity) {
        AppUserEntity newEntity = mapper.map(request, AppUserEntity.class);
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(newEntity, entity);
        return entity;
    }
}
