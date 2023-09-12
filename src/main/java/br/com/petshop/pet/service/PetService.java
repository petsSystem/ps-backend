package br.com.petshop.pet.service;

import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.pet.model.dto.request.PetCreateRequest;
import br.com.petshop.pet.model.dto.request.PetRequest;
import br.com.petshop.pet.model.dto.response.PetResponse;
import br.com.petshop.pet.model.entity.PetEntity;
import br.com.petshop.user.app.model.entity.AppUserEntity;
import br.com.petshop.user.app.service.AppUserService;
import br.com.petshop.user.app.service.ConvertService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Set;

@Service
public class PetService {

    Logger log = LoggerFactory.getLogger(PetService.class);
    @Autowired private AppUserService appUserService;
    @Autowired private ConvertService convert;

    public PetResponse create(Principal authentication, PetCreateRequest request) {
        try {
            AppUserEntity userEntity = appUserService.findByEmail(authentication.getName());
            Set<PetEntity> petEntities = userEntity.getAppUserPets();

            for(PetEntity pet : petEntities) {
                if (pet.getName().equalsIgnoreCase(request.getName()))
                    throw new GenericAlreadyRegisteredException("Pet " + request.getName() + " já cadastrado no sistema.");
            }

            PetEntity petEntity = convert.convertPetCreateRequestIntoEntity(request);
            petEntities.add(petEntity);

            userEntity.setAppUserPets(petEntities);

            appUserService.save(userEntity);

            return convert.convertPetEntityIntoResponse(petEntity);

        } catch (GenericAlreadyRegisteredException ex) {
            log.error("Already registered: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao cadastrar pet. Tente novamente mais tarde.", ex);
        }
    }
}
