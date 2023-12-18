package br.com.petshop.system.profile.service;

import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.system.profile.model.dto.request.ProfileCreateRequest;
import br.com.petshop.system.profile.model.entity.ProfileEntity;
import br.com.petshop.system.profile.repository.ProfileRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ProfileService {

    Logger log = LoggerFactory.getLogger(ProfileService.class);
    @Autowired private ProfileRepository profileRepository;
    @Autowired private ProfileConverterService convert;
    @Autowired private ObjectMapper objectMapper;

    public ProfileEntity create (ProfileCreateRequest request) {
        ProfileEntity profileEntity = profileRepository.findByName(request.getName())
                .orElse(null);

        if (profileEntity != null)
            throw new GenericAlreadyRegisteredException();

        profileEntity = convert.createRequestIntoEntity(request);

        return profileRepository.save(profileEntity);
    }

    public ProfileEntity partialUpdate(UUID profileId, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        ProfileEntity entity = profileRepository.findById(profileId)
                .orElseThrow(GenericNotFoundException::new);

        entity = applyPatch(patch, entity);

        return profileRepository.save(entity);
    }

    private ProfileEntity applyPatch(JsonPatch patch, ProfileEntity entity) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        return objectMapper.treeToValue(patched, ProfileEntity.class);
    }

    public ProfileEntity findById(UUID profileId) {
        return profileRepository.findById(profileId)
                .orElseThrow(GenericNotFoundException::new);
    }

    public ProfileEntity findByName(String name) {
        return profileRepository.findByName(name)
                .orElseThrow(GenericNotFoundException::new);
    }

    public List<ProfileEntity> findAllLabels() {
        return profileRepository.findAll();
    }

    public Page<ProfileEntity> getAll(Pageable pageable) {
        return profileRepository.findAll(pageable);
    }
}
