package br.com.petshop.app.pet.service;

import br.com.petshop.app.pet.model.dto.request.PetCreateRequest;
import br.com.petshop.app.pet.model.enums.Message;
import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.app.pet.model.dto.request.PetUpdateRequest;
import br.com.petshop.app.pet.model.dto.response.PetResponse;
import br.com.petshop.app.pet.model.entity.PetEntity;
import br.com.petshop.app.pet.repository.PetRepository;
import br.com.petshop.app.user.model.entity.AppUserEntity;
import br.com.petshop.app.user.service.AppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PetService {

    Logger log = LoggerFactory.getLogger(PetService.class);
    @Autowired private AppUserService appUserService;
    @Autowired private PetRepository petRepository;
    @Autowired private PetConverterService convert;

    public PetResponse create(Principal authentication, PetCreateRequest request) {
        try {
            List<PetEntity> pets = petRepository.findByAppUser_EmailAndActiveIsTrue(authentication.getName());

            final PetEntity[] petEntity = {null};

            pets.stream()
                    .filter(p -> p.getName().equalsIgnoreCase(request.getName()))
                    .findAny()
                    .ifPresentOrElse(
                            (error) -> {
                                throw new GenericAlreadyRegisteredException(Message.PET_ALREADY_REGISTERED.get());
                            },
                            () -> {
                                PetEntity entity = convert.createRequestIntoEntity(request);
                                AppUserEntity userEntity = appUserService.findByEmail(authentication.getName());
                                entity.setAppUser(userEntity);
                                petEntity[0] = petRepository.save(entity);
                            });

            return convert.entityIntoResponse(petEntity[0]);

        } catch (GenericAlreadyRegisteredException ex) {
            log.error(Message.PET_ALREADY_REGISTERED.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error(Message.PET_ERROR_CREATE.get() + " Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PET_ERROR_CREATE.get(), ex);
        }
    }

    public PetResponse update(Principal authentication, String petId, PetUpdateRequest request) {
        try {
            PetEntity entity = petRepository.findByIdAndActiveIsTrue(petId)
                    .orElseThrow(GenericNotFoundException::new);

            entity = convert.updateRequestIntoEntity(request, entity);
            petRepository.save(entity);

            return convert.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.PET_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,Message.PET_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.PET_ERROR_UPDATE.get() + " Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PET_ERROR_UPDATE.get(), ex);
        }
    }

    public void deactivate(Principal authentication, String petId) {
        try {
            List<PetEntity> pets = petRepository.findByAppUser_EmailAndActiveIsTrue(authentication.getName());

            PetEntity entity = pets.stream()
                    .filter(p -> p.getId().equalsIgnoreCase(petId))
                    .findFirst()
                    .orElseThrow(GenericNotFoundException::new);

            entity.setActive(false);
            petRepository.save(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.PET_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,Message.PET_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.PET_ERROR_DELETE.get() + " Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PET_ERROR_DELETE.get(), ex);
        }
    }

    public Set<PetResponse> get(Principal authentication) {
        try {
            List<PetEntity> pets = petRepository.findByAppUser_EmailAndActiveIsTrue(authentication.getName());

            return pets.stream()
                    .map(p -> convert.entityIntoResponse(p))
                    .collect(Collectors.toSet());

        } catch (Exception ex) {
            log.error(Message.PET_ERROR_GET.get() + " Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PET_ERROR_GET.get(), ex);
        }
    }

    public PetResponse getById(Principal authentication, String petId) {
        try {
            List<PetEntity> pets = petRepository.findByAppUser_EmailAndActiveIsTrue(authentication.getName());

            PetEntity entity = pets.stream()
                    .filter(p -> p.getId().equalsIgnoreCase(petId))
                    .findFirst()
                    .orElseThrow(GenericNotFoundException::new);

            return convert.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.PET_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,Message.PET_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.PET_ERROR_GET.get() + " Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PET_ERROR_GET.get(), ex);
        }
    }
}
