package br.com.petshop.pet.service;

import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.pet.model.dto.request.PetCreateRequest;
import br.com.petshop.pet.model.dto.request.PetUpdateRequest;
import br.com.petshop.pet.model.dto.response.PetResponse;
import br.com.petshop.pet.model.entity.PetEntity;
import br.com.petshop.pet.repository.PetRepository;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PetService {

    Logger log = LoggerFactory.getLogger(PetService.class);
    @Autowired private AppUserService appUserService;
    @Autowired private PetRepository petRepository;
    @Autowired private ConvertService convert;

    public PetResponse create(Principal authentication, PetCreateRequest request) {
        try {
            List<PetEntity> pets = petRepository.findByAppUser_EmailAndActiveIsTrue(authentication.getName());

            final PetEntity[] petEntity = {null};

            pets.stream()
                    .filter(p -> p.getName().equalsIgnoreCase(request.getName()))
                    .findAny()
                    .ifPresentOrElse(
                            (error) -> {
                                throw new GenericAlreadyRegisteredException("Pet " + request.getName() + " já cadastrado(a) no sistema.");
                            },
                            () -> {
                                PetEntity entity = convert.convertPetCreateRequestIntoEntity(request);
                                AppUserEntity userEntity = appUserService.findByEmail(authentication.getName());
                                entity.setAppUser(userEntity);
                                petEntity[0] = petRepository.save(entity);
                            });

            return convert.convertPetEntityIntoResponse(petEntity[0]);

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

    public PetResponse update(Principal authentication, String petId, PetUpdateRequest request) {
        try {
            PetEntity entity = petRepository.findByIdAndActiveIsTrue(petId)
                    .orElseThrow(GenericNotFoundException::new);

            entity = convert.convertPetUpdateRequestIntoEntity(request, entity);
            petRepository.save(entity);

            return convert.convertPetEntityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error("Pet not found: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao atualizar dados do pet. Tente novamente mais tarde.", ex);
        }
    }

    public void deactivate(String petId) {
        try {
            PetEntity entity = petRepository.findByIdAndActiveIsTrue(petId)
                    .orElseThrow(GenericNotFoundException::new);
            entity.setActive(false);
            petRepository.save(entity);

        } catch (GenericNotFoundException ex) {
            log.error("Pet not found: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Cadastro de pet não encontrado.", ex);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao excluir dados do pet. Tente novamente mais tarde.", ex);
        }
    }

    public Set<PetResponse> get(Principal authentication) {
        try {
            List<PetEntity> pets = petRepository.findByAppUser_EmailAndActiveIsTrue(authentication.getName());

            return pets.stream()
                    .map(p -> convert.convertPetEntityIntoResponse(p))
                    .collect(Collectors.toSet());

        } catch (GenericNotFoundException ex) {
            log.error("Pet not found: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao recuperar dados do pet. Tente novamente mais tarde.", ex);
        }
    }
}
