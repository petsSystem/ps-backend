package br.com.petshop.user.service;

import br.com.petshop.dao.entity.AddressEntity;
import br.com.petshop.dao.entity.AppUserEntity;
import br.com.petshop.dao.entity.PetEntity;
import br.com.petshop.model.dto.request.AppUserRequest;
import br.com.petshop.model.dto.request.PetRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ConvertService {

    @Autowired
    private ModelMapper mapper;

    public AppUserEntity convertAppUserRequestIntoEntity(AppUserRequest request) {
        AppUserEntity entity = mapper.map(request, AppUserEntity.class);
        AddressEntity addressEntity = mapper.map(request.getAddress(), AddressEntity.class);
        entity.setAppUserAddresses(Set.of(addressEntity));
        return entity;
    }

    public PetEntity convertPetRequestIntoEntity(PetRequest request) {
        return mapper.map(request, PetEntity.class);
    }

}
