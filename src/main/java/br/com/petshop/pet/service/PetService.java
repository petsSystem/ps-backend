package br.com.petshop.pet.service;

import br.com.petshop.commons.exception.GenericNotFoundException;
import br.com.petshop.pet.model.entity.PetEntity;
import br.com.petshop.pet.repository.PetRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Classe responsável pelos serviços de pets
 */
@Service
public class PetService {

    private Logger log = LoggerFactory.getLogger(PetService.class);
    @Autowired private PetRepository repository;
    @Autowired private ObjectMapper objectMapper;

    public PetEntity create(PetEntity request) {
        return repository.save(request);
    }

    /**
     * Método de criação de pet
     * @param petId - id de cadastro do pet
     * @return - entidade de pet
     */
    public PetEntity findByIdAndActiveIsTrue(UUID petId) {
        return repository.findByIdAndActiveIsTrue(petId)
                .orElseThrow(GenericNotFoundException::new);
    }

    /**
     * Método de atualização de pet.
     * @param entity - entidade de pet
     * @return - entidade de pet
     */
    public PetEntity updateById(PetEntity entity) {
        return repository.save(entity);
    }

    /**
     * Método que recupera os pets de um cliente informado.
     * @param customerId - id de cadastro do cliente
     * @return - lista de entidades de pet
     */
    public List<PetEntity> getByCustomerId(UUID customerId) {
        return repository.findByCustomerIdAndActiveIsTrue(customerId);
    }

    /**
     * Métoddo que recupera os dados do pet através da informação do id.
     * @param petId - id de cadastro do pet
     * @return - entidade de pet
     */
    public PetEntity getById(UUID petId) {
        return repository.findByIdAndActiveIsTrue(petId)
                .orElseThrow(GenericNotFoundException::new);
    }

    /**
     * Método que desativa um pet (exclusão lógica).
     * @param entity - entidade de pet
     * @param patch - dados de desativação do pet
     * @return - entidade de pet
     * @throws JsonPatchException
     * @throws JsonProcessingException
     */
    public PetEntity deactivate (PetEntity entity, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        entity = objectMapper.treeToValue(patched, PetEntity.class);

        return repository.save(entity);
    }
}
