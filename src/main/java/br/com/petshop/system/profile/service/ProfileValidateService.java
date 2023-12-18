package br.com.petshop.system.profile.service;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.system.profile.model.dto.request.ProfileCreateRequest;
import br.com.petshop.system.profile.model.dto.response.LabelResponse;
import br.com.petshop.system.profile.model.dto.response.ProfileResponse;
import br.com.petshop.system.profile.model.entity.ProfileEntity;
import br.com.petshop.system.profile.model.enums.Message;
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
public class ProfileValidateService {

    Logger log = LoggerFactory.getLogger(ProfileValidateService.class);
    @Autowired private ProfileService service;
    @Autowired private ProfileConverterService convert;

    @Autowired private ObjectMapper objectMapper;

    public ProfileResponse create (Principal authentication, ProfileCreateRequest request) {
        try {
            ProfileEntity accessGroupEntity = service.create(request);

            return convert.entityIntoResponse(accessGroupEntity);

        } catch (GenericAlreadyRegisteredException ex) {
            log.error(Message.PROFILE_ALREADY_REGISTERED_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, Message.PROFILE_ALREADY_REGISTERED_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.PROFILE_CREATE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PROFILE_CREATE_ERROR.get(), ex);
        }
    }

    public  List<ProfileResponse> partialUpdate(Principal authentication, UUID profileId, JsonPatch patch) {
        try {
            JsonNode jsonPatchList = objectMapper.convertValue(patch, JsonNode.class);
            List<ProfileResponse> responses = new ArrayList<>();
            for(int i = 0; i < jsonPatchList.size(); i++) {

                ProfileEntity entity = service.partialUpdate(profileId, patch);

                responses.add(convert.entityIntoResponse(entity));
            }

            return responses;

        } catch (GenericNotFoundException ex) {
            log.error(Message.PROFILE_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.PROFILE_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.PROFILE_UPDATE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PROFILE_UPDATE_ERROR.get(), ex);
        }
    }

    public Page<ProfileResponse> get(Principal authentication, Pageable pageable) {
        try {
            Page<ProfileEntity> entities = service.getAll(pageable);

            List<ProfileResponse> response = entities.stream()
                    .map(c -> convert.entityIntoResponse(c))
                    .collect(Collectors.toList());

            return new PageImpl<>(response);

        } catch (Exception ex) {
            log.error(Message.PROFILE_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PROFILE_GET_ERROR.get(), ex);
        }
    }

    public List<LabelResponse> getLabels(Principal authentication) {
        try {

            List<ProfileEntity> entities = service.findAllLabels();

            if (getRole(authentication) != Role.ADMIN) {
                entities = entities.stream()
                        .filter(l -> !l.getName().equalsIgnoreCase("Administrador") ||
                                !l.getName().equalsIgnoreCase("Proprietário"))
                        .collect(Collectors.toList());
            }

            return entities.stream().
                    map(e -> convert.entityIntoLabelResponse(e))
                    .collect(Collectors.toList());

        } catch (Exception ex) {
            log.error(Message.PROFILE_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PROFILE_GET_ERROR.get(), ex);
        }
    }

    public ProfileResponse getById(UUID profileId) {
        try {
            ProfileEntity entity = service.findById(profileId);

            return convert.entityIntoResponse(entity);

        } catch (Exception ex) {
            log.error(Message.PROFILE_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PROFILE_GET_ERROR.get(), ex);
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
