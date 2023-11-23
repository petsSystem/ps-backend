package br.com.petshop.system.access.service;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.system.access.model.dto.request.AccessGroupCreateRequest;
import br.com.petshop.system.access.model.dto.response.AccessGroupResponse;
import br.com.petshop.system.access.model.entity.AccessGroupEntity;
import br.com.petshop.system.access.model.enums.Message;
import br.com.petshop.system.user.model.entity.SysUserEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
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

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccessGroupValidateService {

    Logger log = LoggerFactory.getLogger(AccessGroupValidateService.class);
    @Autowired private AccessGroupService service;
    @Autowired private AccessConverterService convert;

    @Autowired private ObjectMapper objectMapper;

    public AccessGroupResponse create (Principal authentication, AccessGroupCreateRequest request) {
        try {
            AccessGroupEntity accessGroupEntity = service.create(request);

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

    public  List<AccessGroupResponse> partialUpdate(Principal authentication, UUID accessGroupId, JsonPatch patch) {
        try {
            JsonNode jsonPatchList = objectMapper.convertValue(patch, JsonNode.class);
            List<AccessGroupResponse> responses = new ArrayList<>();
            for(int i = 0; i < jsonPatchList.size(); i++) {

                AccessGroupEntity entity = service.partialUpdate(accessGroupId, patch);

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

    public List<AccessGroupResponse> get(Principal authentication) {
        try {

            List<AccessGroupEntity> entities = new ArrayList<>();
            for(UUID accessGroupId : getAuthUser(authentication).getAccessGroupIds()) {
                AccessGroupEntity entity = service.findById(accessGroupId);
                entities.add(entity);
            }

            return entities.stream()
                    .map(c -> convert.entityIntoResponse(c))
                    .collect(Collectors.toList());

        } catch (Exception ex) {
            log.error(Message.ACCESS_GROUP_ERROR_GET.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.ACCESS_GROUP_ERROR_GET.get(), ex);
        }
    }

    public Page<AccessGroupResponse> getAll(Principal authentication, Pageable pageable) {
        try {
            Page<AccessGroupEntity> entities = service.getAll(pageable);

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

    public AccessGroupResponse getById(Principal authentication, UUID accessGroupId) {
        try {
            AccessGroupEntity entity = service.findById(accessGroupId);

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

    public void delete(Principal authentication, UUID accessGroupId) {
        try {

            service.delete(accessGroupId);

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

    private SysUserEntity getAuthUser(Principal authentication) {
        return ((SysUserEntity) ((UsernamePasswordAuthenticationToken)
                authentication).getPrincipal());
    }

    private Role getRole(Principal authentication) {
        SysUserEntity systemUser = ((SysUserEntity) ((UsernamePasswordAuthenticationToken)
                authentication).getPrincipal());

        return systemUser.getRole();
    }
}
