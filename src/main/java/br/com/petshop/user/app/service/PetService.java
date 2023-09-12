package br.com.petshop.user.app.service;

import br.com.petshop.user.app.model.entity.AppUserEntity;
import br.com.petshop.pet.model.entity.PetEntity;
import br.com.petshop.pet.model.dto.request.PetRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class PetService {
    @Autowired private AppUserService appUserService;
    @Autowired private ConvertService convert;

    public void create(PetRequest request, String email) {
        AppUserEntity appUserEntity = appUserService.findByEmail(email);

        Set<PetEntity> petEntities = appUserEntity.getAppUserPets();

        for(PetEntity pet : petEntities) {
            if (pet.getName().equalsIgnoreCase(request.getName()))
                throw new RuntimeException("There is already a pet with this name");
        }

        PetEntity petEntity = convert.convertPetRequestIntoEntity(request);
        petEntities.add(petEntity);

        appUserEntity.setAppUserPets(petEntities);

        appUserService.save(appUserEntity);
    }
}
