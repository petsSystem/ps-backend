package br.com.petshop.system.profile.service;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.system.profile.model.dto.request.ProfileCreateRequest;
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
import java.util.Collections;
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
            log.error(Message.PROFILE_ALREADY_REGISTERED.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, Message.PROFILE_ALREADY_REGISTERED.get(), ex);
        } catch (Exception ex) {
            log.error(Message.PROFILE_ERROR_CREATE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PROFILE_ERROR_CREATE.get(), ex);
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
            log.error(Message.PROFILE_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.PROFILE_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.PROFILE_ERROR_PARTIAL.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PROFILE_ERROR_PARTIAL.get(), ex);
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
            log.error(Message.PROFILE_ERROR_GET.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PROFILE_ERROR_GET.get(), ex);
        }
    }

    public List<String> getLabels(Principal authentication) {
        try {

            List<String> labels = service.findAllLabels();

            if (getRole(authentication) != Role.ADMIN) {
                labels.remove("Administrador");
                labels.remove("Proprietário");
            }

            Collections.sort(labels);

            return labels;

        } catch (Exception ex) {
            log.error(Message.PROFILE_ERROR_GET.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PROFILE_ERROR_GET.get(), ex);
        }
    }

    public void delete(Principal authentication, UUID profileId) {
        try {

            service.delete(profileId);

        } catch (GenericNotFoundException ex) {
            log.error(Message.PROFILE_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.PROFILE_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.PROFILE_ERROR_DELETE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PROFILE_ERROR_DELETE.get(), ex);
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
