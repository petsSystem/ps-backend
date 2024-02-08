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

@Service
public class PetService {

    private Logger log = LoggerFactory.getLogger(PetService.class);
    @Autowired private PetRepository repository;
    @Autowired private PetConverterService converter;
    @Autowired private ObjectMapper objectMapper;

    public PetEntity create(PetEntity request) {
        return repository.save(request);
    }

    public PetEntity update(UUID petId, PetEntity request) {
        PetEntity entity = repository.findByIdAndActiveIsTrue(petId)
                .orElseThrow(GenericNotFoundException::new);

        entity = converter.updateRequestIntoEntity(request, entity);
        return repository.save(entity);
    }

    public List<PetEntity> getByCustomerId(UUID customerId) {
        return repository.findByCustomerIdAndActiveIsTrue(customerId);
    }

    public PetEntity getById(UUID petId) {
        return repository.findByIdAndActiveIsTrue(petId)
                .orElseThrow(GenericNotFoundException::new);
    }

    public PetEntity deactivate (UUID petId, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        PetEntity entity = repository.findByIdAndActiveIsTrue(petId)
                .orElseThrow(GenericNotFoundException::new);

        entity = applyPatch(patch, entity);

        return repository.save(entity);
    }

    private PetEntity applyPatch(JsonPatch patch, PetEntity entity) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        return objectMapper.treeToValue(patched, PetEntity.class);
    }
}
