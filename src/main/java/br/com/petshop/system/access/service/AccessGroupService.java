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

    public AccessGroupResponse create (AccessGroupCreateRequest request) {
        try {
            AccessGroupEntity accessGroupEntity = accessGroupRepository.findByName(request.getName())
                    .orElse(null);

            if (accessGroupEntity != null)
                throw new GenericAlreadyRegisteredException();

            accessGroupEntity = convert.createRequestIntoEntity(request);
            accessGroupRepository.save(accessGroupEntity);

            return convert.entityIntoResponse(accessGroupEntity);
        } catch (GenericAlreadyRegisteredException ex) {
            log.error(Message.ACCESS_GROUP_ALREADY_REGISTERED.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, Message.ACCESS_GROUP_ALREADY_REGISTERED.get(), ex);
        } catch (Exception ex) {
            log.error(Message.ACCESS_GROUP_ERROR_CREATE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.ACCESS_GROUP_ERROR_CREATE.get(), ex);
        }
    }

    public  List<AccessGroupResponse> partialUpdate(UUID accessGroupId, JsonPatch patch) {
        try {
            JsonNode jsonPatchList = objectMapper.convertValue(patch, JsonNode.class);
            List<AccessGroupResponse> responses = new ArrayList<>();
            for(int i = 0; i < jsonPatchList.size(); i++) {

                AccessGroupEntity entity = accessGroupRepository.findById(accessGroupId)
                        .orElseThrow(GenericNotFoundException::new);

                entity = applyPatch(patch, entity);
                entity = accessGroupRepository.save(entity);

                responses.add(convert.entityIntoResponse(entity));
            }
            return responses;

        } catch (GenericNotFoundException ex) {
            log.error(Message.ACCESS_GROUP_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.ACCESS_GROUP_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.ACCESS_GROUP_ERROR_PARTIAL.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.ACCESS_GROUP_ERROR_PARTIAL.get(), ex);
        }
    }

    private AccessGroupEntity applyPatch(JsonPatch patch, AccessGroupEntity entity) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        return objectMapper.treeToValue(patched, AccessGroupEntity.class);
    }

    public Page<AccessGroupResponse> getAll(Pageable pageable) {
        try {
            Page<AccessGroupEntity> entities = accessGroupRepository.findAll(pageable);

            List<AccessGroupResponse> response = entities.stream()
                    .map(c -> convert.entityIntoResponse(c))
                    .collect(Collectors.toList());

            return new PageImpl<>(response);

        } catch (Exception ex) {
            log.error(Message.ACCESS_GROUP_ERROR_GET.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.ACCESS_GROUP_ERROR_GET.get(), ex);
        }
    }

    public AccessGroupResponse getById(UUID accessGroupId) {
        try {
            AccessGroupEntity entity = accessGroupRepository.findById(accessGroupId)
                    .orElseThrow(GenericNotFoundException::new);

            return convert.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.ACCESS_GROUP_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.ACCESS_GROUP_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.ACCESS_GROUP_ERROR_GET.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.ACCESS_GROUP_ERROR_GET.get(), ex);
        }
    }

    public void delete(UUID accessGroupId) {
        try {
            AccessGroupEntity entity = accessGroupRepository.findById(accessGroupId)
                    .orElseThrow(GenericNotFoundException::new);
            accessGroupRepository.delete(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.ACCESS_GROUP_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.ACCESS_GROUP_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.ACCESS_GROUP_ERROR_DELETE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.ACCESS_GROUP_ERROR_DELETE.get(), ex);
        }
    }
}
