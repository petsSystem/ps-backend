package br.com.petshop.pet.service;

import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.pet.model.Breed;
import br.com.petshop.pet.model.dto.request.PetCreateRequest;
import br.com.petshop.pet.model.dto.request.PetUpdateRequest;
import br.com.petshop.pet.model.dto.response.PetResponse;
import br.com.petshop.pet.model.entity.PetEntity;
import br.com.petshop.pet.model.enums.BreedType;
import br.com.petshop.pet.model.enums.Message;
import com.github.fge.jsonpatch.JsonPatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PetFacadeService {

    private Logger log = LoggerFactory.getLogger(PetFacadeService.class);
    @Autowired private PetService service;
    @Autowired private PetConverterService converter;

    public List<Breed> getDogsList(Principal authentication) {
        try {
            return BreedType.dogs().stream()
                    .map(b -> Breed.builder()
                            .name(BreedType.getName(b))
                            .size(BreedType.getSize(b))
                            .build())
                    .collect(Collectors.toList());

        } catch (Exception ex) {
            log.error(Message.PET_LIST_ERROR.get() + " Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PET_LIST_ERROR.get(), ex);
        }
    }

    public List<Breed> getCatsList(Principal authentication) {
        try {
            return BreedType.cats().stream()
                    .map(b -> Breed.builder()
                            .name(BreedType.getName(b))
                            .size(BreedType.getSize(b))
                            .build())
                    .collect(Collectors.toList());

        } catch (Exception ex) {
            log.error(Message.PET_LIST_ERROR.get() + " Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PET_LIST_ERROR.get(), ex);
        }
    }

    public PetResponse create(Principal authentication, PetCreateRequest request) {
        try {
            PetEntity entity = converter.createRequestIntoEntity(request);
            entity = service.create(entity);

            return converter.entityIntoResponse(entity);

        } catch (Exception ex) {
            log.error(Message.PET_CREATE_ERROR.get() + " Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PET_CREATE_ERROR.get(), ex);
        }
    }

    public PetResponse update(Principal authentication, UUID petId, PetUpdateRequest request) {
        try {
            PetEntity entity = converter.updateRequestIntoEntity(request);
            entity = service.update(petId, entity);

            return converter.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.PET_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,Message.PET_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.PET_UPDATE_ERROR.get() + " Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PET_UPDATE_ERROR.get(), ex);
        }
    }

    public Set<PetResponse> getByCustomerId(Principal authentication, UUID customerId) {
        try {
            List<PetEntity> pets = service.getByCustomerId(customerId);
            return pets.stream()
                    .map(p -> converter.entityIntoResponse(p))
                    .collect(Collectors.toSet());

        } catch (Exception ex) {
            log.error(Message.PET_GET_ERROR.get() + " Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PET_GET_ERROR.get(), ex);
        }
    }

    public PetResponse getById(Principal authentication, UUID petId) {
        try {
            PetEntity entity = service.getById(petId);

            return converter.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.PET_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,Message.PET_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.PET_GET_ERROR.get() + " Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PET_GET_ERROR.get(), ex);
        }
    }

    public PetResponse deactivate (Principal authentication, UUID petId, JsonPatch patch) {
        try {

            PetEntity entity = service.deactivate(petId, patch);

            return  converter.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.PET_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.PET_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.PET_DELETE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PET_DELETE_ERROR.get(), ex);
        }
    }
}
