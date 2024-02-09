package br.com.petshop.profile.service;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.commons.exception.GenericAlreadyRegisteredException;
import br.com.petshop.commons.exception.GenericNotFoundException;
import br.com.petshop.profile.model.dto.request.ProfileCreateRequest;
import br.com.petshop.profile.model.dto.request.ProfileUpdateRequest;
import br.com.petshop.profile.model.dto.response.ProfileLabelResponse;
import br.com.petshop.profile.model.dto.response.ProfileResponse;
import br.com.petshop.profile.model.entity.ProfileEntity;
import br.com.petshop.profile.model.enums.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProfileBusinessService {
    private Logger log = LoggerFactory.getLogger(ProfileBusinessService.class);
    @Autowired private ProfileService service;
    @Autowired private ProfileConverterService converter;
    @Autowired private ProfileValidationService validate;

    public ProfileResponse create (Principal authentication, ProfileCreateRequest request) {
        try {
            ProfileEntity entityRequest = converter.createRequestIntoEntity(request);

            ProfileEntity entity = service.create(entityRequest);

            return converter.entityIntoResponse(entity);

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

    public ProfileResponse updateById(Principal authentication, UUID profileId, ProfileUpdateRequest request) {
        try {
            ProfileEntity entity = service.findById(profileId);
            ProfileEntity entityRequest = converter.updateRequestIntoEntity(request);

            entity = converter.updateRequestIntoEntity(entityRequest, entity);

            entity = service.save(entity);

            return converter.entityIntoResponse(entity);

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
                    .map(c -> converter.entityIntoResponse(c))
                    .collect(Collectors.toList());

            return new PageImpl<>(response);

        } catch (Exception ex) {
            log.error(Message.PROFILE_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PROFILE_GET_ERROR.get(), ex);
        }
    }

    public ProfileResponse getById(Principal authentication, UUID profileId) {
        return getById(profileId);
    }
    public ProfileResponse getById(UUID profileId) {
        try {
            ProfileEntity entity = service.findById(profileId);

            return converter.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.PROFILE_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.PROFILE_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.PROFILE_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PROFILE_GET_ERROR.get(), ex);
        }
    }

    public List<ProfileLabelResponse> getLabels(Principal authentication) {
        try {

            List<ProfileEntity> entities = service.findAllLabels();

            validate.accessLabels(authentication, entities);

            return entities.stream().
                    map(e -> converter.entityIntoLabelResponse(e))
                    .collect(Collectors.toList());

        } catch (Exception ex) {
            log.error(Message.PROFILE_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PROFILE_GET_ERROR.get(), ex);
        }
    }

    public List<ProfileResponse> getByIds(List<UUID> profileIds) {
        try {
            return profileIds.stream()
                    .map(p -> getById(p))
                    .collect(Collectors.toList());

        } catch (Exception ex) {
            log.error(Message.PROFILE_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PROFILE_GET_ERROR.get(), ex);
        }
    }

    public Role getCommonRole(List<UUID> profileIds) {
        try {
            List<Role> roles = profileIds.stream()
                    .map(p -> getById(p).getRole())
                    .collect(Collectors.toList());

            Role finalRole = roles.get(0);

            if (roles.size() > 1 && finalRole != Role.ADMIN) {
                for (int i = 1; i < roles.size(); i++) {
                    Role role = roles.get(i);

                    if (role == Role.ADMIN)
                        return role;

                    if (role == Role.OWNER && finalRole != Role.ADMIN)
                        finalRole = role;

                    if (role == Role.MANAGER && finalRole != Role.ADMIN && finalRole != Role.OWNER)
                        finalRole = role;
                }
            }

            return finalRole;

        } catch (Exception ex) {
            log.error(Message.PROFILE_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PROFILE_GET_ERROR.get(), ex);
        }
    }
}
