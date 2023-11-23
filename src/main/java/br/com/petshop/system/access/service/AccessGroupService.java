package br.com.petshop.system.access.service;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericForbiddenException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.system.access.model.dto.request.AccessGroupCreateRequest;
import br.com.petshop.system.access.model.dto.response.AccessGroupResponse;
import br.com.petshop.system.access.model.entity.AccessGroupEntity;
import br.com.petshop.system.access.model.enums.Message;
import br.com.petshop.system.access.repository.AccessGroupRepository;
import br.com.petshop.system.company.model.dto.response.CompanyResponse;
import br.com.petshop.system.user.model.entity.SysUserEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccessGroupService {

    Logger log = LoggerFactory.getLogger(AccessGroupService.class);
    @Autowired private AccessGroupRepository accessGroupRepository;
    @Autowired private AccessConverterService convert;

    @Autowired private ObjectMapper objectMapper;

    public AccessGroupEntity create (AccessGroupCreateRequest request) {
        AccessGroupEntity accessGroupEntity = accessGroupRepository.findByName(request.getName())
                .orElse(null);

        if (accessGroupEntity != null)
            throw new GenericAlreadyRegisteredException();

        accessGroupEntity = convert.createRequestIntoEntity(request);

        return accessGroupRepository.save(accessGroupEntity);
    }

    public  AccessGroupEntity partialUpdate(UUID accessGroupId, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        AccessGroupEntity entity = accessGroupRepository.findById(accessGroupId)
                .orElseThrow(GenericNotFoundException::new);

        entity = applyPatch(patch, entity);

        return accessGroupRepository.save(entity);
    }

    private AccessGroupEntity applyPatch(JsonPatch patch, AccessGroupEntity entity) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        return objectMapper.treeToValue(patched, AccessGroupEntity.class);
    }

    public AccessGroupEntity findById(UUID accessGroupId) {
        return accessGroupRepository.findById(accessGroupId)
                .orElseThrow(GenericNotFoundException::new);
    }

    public Page<AccessGroupEntity> getAll(Pageable pageable) {
        return accessGroupRepository.findAll(pageable);
    }

    public void delete(UUID accessGroupId) {
        AccessGroupEntity entity = accessGroupRepository.findById(accessGroupId)
                .orElseThrow(GenericNotFoundException::new);
        accessGroupRepository.delete(entity);
    }
}
