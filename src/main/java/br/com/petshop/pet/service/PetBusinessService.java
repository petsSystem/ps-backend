package br.com.petshop.pet.service;

import br.com.petshop.commons.exception.GenericNotFoundException;
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

/**
 * Classe responsável pelas regras de negócio do pet
 */
@Service
public class PetBusinessService {

    private final Logger log = LoggerFactory.getLogger(PetBusinessService.class);
    @Autowired private PetService service;
    @Autowired private PetConverterService converter;

    /**
     * Método que recupera uma lista de raças de cachorros
     * @param authentication - dados do usuário logado
     * @return - lista de raças de cachorros
     */
    public List<String> getDogsList(Principal authentication) {
        try {
            return BreedType.dogValues();

        } catch (Exception ex) {
            log.error(Message.PET_LIST_ERROR.get() + " Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PET_LIST_ERROR.get(), ex);
        }
    }

    /**
     * Método que recupera uma lista de raças de gatos
     * @param authentication - dados do usuário logado
     * @return - lista de raças de gatos
     */
    public List<String> getCatsList(Principal authentication) {
        try {
            return BreedType.catValues();

        } catch (Exception ex) {
            log.error(Message.PET_LIST_ERROR.get() + " Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PET_LIST_ERROR.get(), ex);
        }
    }

    /**
     * Método de criação de pet.
     * @param authentication - dados do usuário logado
     * @param request - dto contendo dados de criação do pet
     * @return - dados do pet
     */
    public PetResponse create(Principal authentication, PetCreateRequest request) {
        try {
            //converte request em entidade
            PetEntity entity = converter.createRequestIntoEntity(request);

            //cria a entidade pet
            entity = service.create(entity);

            //converte a entidade na resposta final
            return converter.entityIntoResponse(entity);

        } catch (Exception ex) {
            log.error(Message.PET_CREATE_ERROR.get() + " Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PET_CREATE_ERROR.get(), ex);
        }
    }

    /**
     * Método de atualização de pet.
     * @param authentication - dados do usuário logado
     * @param petId - id do cadastro do pet
     * @param request - dto contendo dados de atualização do pet
     * @return - dados do pet
     */
    public PetResponse update(Principal authentication, UUID petId, PetUpdateRequest request) {
        try {
            //recupera do pet ativo pelo id
            PetEntity entity = service.findByIdAndActiveIsTrue(petId);

            //converte request em entidade
            PetEntity entityRequest = converter.updateRequestIntoEntity(request);
            entity = converter.updateRequestIntoEntity(entityRequest, entity);

            //faz atualizaçao da entidade
            entity = service.updateById(entity);

            //converte a entidade na resposta final
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

    /**
     * Método que recupera os pets de um cliente informado.
     * @param authentication - dados do usuário logado
     * @param customerId - id do cadastro do cliente/tutor
     * @return - lista de dados de pets
     */
    public Set<PetResponse> getByCustomerId(Principal authentication, UUID customerId) {
        try {
            //recupera todos os pets pelo customerId
            List<PetEntity> pets = service.getByCustomerId(customerId);

            //converte a entidade na resposta final
            return pets.stream()
                    .map(p -> converter.entityIntoResponse(p))
                    .collect(Collectors.toSet());

        } catch (Exception ex) {
            log.error(Message.PET_GET_ERROR.get() + " Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PET_GET_ERROR.get(), ex);
        }
    }

    /**
     * Métoddo que recupera os dados do pet através da informação do id.
     * @param authentication - dados do usuário logado
     * @param petId - id do cadastro do pet
     * @return - dados do pet
     */
    public PetResponse getById(Principal authentication, UUID petId) {
        try {
            //recupera o pet pelo id
            PetEntity entity = service.getById(petId);

            //converte a entidade na resposta final
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

    /**
     * Método que desativa um pet (exclusão lógica).
     * @param authentication - dados do usuário logado
     * @param petId - id do cadastro do pet
     * @param patch - informações da atualização
     * @return - dados do pet
     */
    public PetResponse deactivate (Principal authentication, UUID petId, JsonPatch patch) {
        try {
            //recupera o pet pelo id
            PetEntity entity = service.getById(petId);

            //desativa o pet
            entity = service.deactivate(entity, patch);

            //converte a entidade na resposta final
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
