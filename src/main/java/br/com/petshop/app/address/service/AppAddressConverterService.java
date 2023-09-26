package br.com.petshop.app.address.service;

import br.com.petshop.app.address.model.dto.request.AppAddressCreateRequest;
import br.com.petshop.app.address.model.dto.request.AppAddressUpdateRequest;
import br.com.petshop.app.address.model.dto.response.AppAddressResponse;
import br.com.petshop.app.address.model.entity.AppAddressEntity;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppAddressConverterService {

    @Autowired
    private ModelMapper mapper;

    public AppAddressEntity addressCreateRequestIntoEntity(AppAddressCreateRequest request) {
        return mapper.map(request, AppAddressEntity.class);
    }

    public AppAddressEntity addressUpdateRequestIntoEntity(AppAddressUpdateRequest request, AppAddressEntity entity) {
        AppAddressEntity newEntity = mapper.map(request, AppAddressEntity.class);
        mapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mapper.map(newEntity, entity);
        return entity;
    }

    public AppAddressResponse addressEntityIntoResponse(AppAddressEntity entity) {
        return mapper.map(entity, AppAddressResponse.class);
    }
}
