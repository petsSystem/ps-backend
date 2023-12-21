package br.com.petshop.system.user.service;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericForbiddenException;
import br.com.petshop.exception.GenericIncorrectPasswordException;
import br.com.petshop.exception.GenericNotActiveException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.system.company.model.entity.CompanyEntity;
import br.com.petshop.system.profile.model.dto.Permission;
import br.com.petshop.system.profile.model.dto.response.ProfileResponse;
import br.com.petshop.system.user.model.dto.request.SysUserCreateRequest;
import br.com.petshop.system.user.model.dto.request.SysUserUpdateRequest;
import br.com.petshop.system.user.model.dto.response.SysUserMeResponse;
import br.com.petshop.system.user.model.dto.response.SysUserProfileResponse;
import br.com.petshop.system.user.model.dto.response.SysUserResponse;
import br.com.petshop.system.user.model.dto.response.SysUserTableResponse;
import br.com.petshop.system.user.model.entity.SysUserEntity;
import br.com.petshop.system.user.model.enums.Message;
import br.com.petshop.system.user.model.dto.request.SysUserForgetRequest;
import br.com.petshop.system.user.model.dto.request.SysUserPasswordRequest;
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
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SysUserValidateService {
    Logger log = LoggerFactory.getLogger(SysUserValidateService.class);
    @Autowired private SysUserService service;
    @Autowired private SysUserConverterService convert;


    public SysUserResponse create(Principal authentication, SysUserCreateRequest request) {
        try {
            SysUserEntity entity = convert.createRequestIntoEntity(request);
            entity = service.create(entity);

            List<ProfileResponse> profiles = service.getProfiles(entity);

            SysUserResponse response = convert.entityIntoResponse(entity);
            response.setProfiles(profiles);

            return response;

        } catch (GenericAlreadyRegisteredException ex) {
            log.error(Message.USER_ALREADY_REGISTERED_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, Message.USER_ALREADY_REGISTERED_ERROR.get(), ex);
        } catch (GenericNotFoundException ex) {
            log.error(Message.USER_COMPANY_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.USER_COMPANY_NOT_FOUND_ERROR.get(), ex);
        } catch (GenericNotActiveException ex) {
            log.error(Message.USER_COMPANY_NOT_ACTIVE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_COMPANY_NOT_ACTIVE_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.USER_CREATE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_CREATE_ERROR.get(), ex);
        }
    }

    public void forget (SysUserForgetRequest request) {
        try {
            service.forget(request.email());

        } catch (GenericNotFoundException ex) {
            log.error(Message.USER_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.USER_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.USER_SENDING_PASSWORD_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_SENDING_PASSWORD_ERROR.get(), ex);
        }
    }

    public SysUserResponse changePassword(Principal authentication,SysUserPasswordRequest request) {
        try {
            SysUserEntity user = getAuthUser(authentication);
            SysUserEntity entity = service.changePassword(user.getId(), request);

            return convert.entityIntoResponse(entity);

        } catch (GenericIncorrectPasswordException ex) {
            log.error(br.com.petshop.system.user.model.enums.Message.USER_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, br.com.petshop.system.user.model.enums.Message.USER_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(br.com.petshop.system.user.model.enums.Message.USER_ACTIVATE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, br.com.petshop.system.user.model.enums.Message.USER_ACTIVATE_ERROR.get(), ex);
        }
    }

    public SysUserProfileResponse getProfile(Principal authentication) {
        try {
            SysUserEntity entity = getAuthUser(authentication);
            entity = service.findById(entity.getId());

            return convert.entityIntoProfileResponse(entity);

        } catch (Exception ex) {
            log.error(Message.USER_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_GET_ERROR.get(), ex);
        }
    }

    public SysUserMeResponse getMeInfo(Principal authentication) {
        try {
            SysUserEntity entity = getAuthUser(authentication);
            entity = service.findById(entity.getId());

            CompanyEntity companyEntity = service.getFirstCompany(entity);

            List<ProfileResponse> profiles = service.getProfiles(entity);
            List<Permission> permissions = profiles.stream()
                    .flatMap(p -> p.getPermissions().stream())
                    .collect(Collectors.toList());

            SysUserMeResponse response = convert.entityIntoMeResponse(entity);
            response.setPermissions(permissions);
            response.setCompanyId(companyEntity.getId());
            response.setCompanyName(companyEntity.getName());

            return response;

        } catch (Exception ex) {
            log.error(Message.USER_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_GET_ERROR.get(), ex);
        }
    }

    public SysUserResponse activate(Principal authentication, UUID userId, JsonPatch patch) {
        try {

            SysUserEntity entity = service.active(userId, patch);

            List<ProfileResponse> profiles = service.getProfiles(entity);

            SysUserResponse response = convert.entityIntoResponse(entity);
            response.setProfiles(profiles);

            return response;

        } catch (GenericNotFoundException ex) {
            log.error(Message.USER_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.USER_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.USER_ACTIVATE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_ACTIVATE_ERROR.get(), ex);
        }
    }

    public SysUserResponse updateById(Principal authentication, UUID userId, SysUserUpdateRequest request) {
        try {
            SysUserEntity entity = service.findByIdAndActiveIsTrue(userId);

            validateUserAccess(getRole(authentication), getAuthUser(authentication), entity);

            SysUserEntity entityRequest = convert.updateRequestIntoEntity(request);

            entity = service.updateById(entityRequest, entity);

            List<ProfileResponse> profiles = service.getProfiles(entity);

            SysUserResponse response = convert.entityIntoResponse(entity);
            response.setProfiles(profiles);

            return response;

        } catch (GenericNotFoundException ex) {
            log.error(Message.USER_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.USER_NOT_FOUND_ERROR.get(), ex);
        } catch (GenericForbiddenException ex) {
            log.error(Message.USER_FORBIDDEN_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, Message.USER_FORBIDDEN_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.USER_UPDATE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_UPDATE_ERROR.get(), ex);
        }
    }

    public  Page<SysUserTableResponse> get(Principal authentication, UUID companyId, Pageable pageable) {
        try {
            SysUserEntity user = getAuthUser(authentication);

            if (getRole(authentication) != Role.ADMIN)
                validateCompanyIdsAccess(user.getCompanyIds(), companyId);

            Page<SysUserEntity> entities = service.findAllByCompanyId(companyId, pageable);

            List<SysUserTableResponse> response = entities.stream()
                    .map(c -> convert.entityIntoTableResponse(c))
                    .collect(Collectors.toList());

            Collections.sort(response, Comparator.comparing(SysUserTableResponse::getActive).reversed()
                    .thenComparing(SysUserTableResponse::getName));

            return new PageImpl<>(response);

        } catch (Exception ex) {
            log.error(Message.USER_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_GET_ERROR.get(), ex);
        }
    }

    public SysUserResponse getById(Principal authentication, UUID userId) {
        try {
            SysUserEntity entity = service.findById(userId);

            validateUserAccess(getRole(authentication), getAuthUser(authentication), entity);

            List<ProfileResponse> profiles = service.getProfiles(entity);

            SysUserResponse response = convert.entityIntoResponse(entity);
            response.setProfiles(profiles);

            return response;

        } catch (GenericNotFoundException ex) {
            log.error(Message.USER_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.USER_NOT_FOUND_ERROR.get(), ex);
        } catch (GenericForbiddenException ex) {
            log.error(Message.USER_FORBIDDEN_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, Message.USER_FORBIDDEN_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.USER_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_GET_ERROR.get(), ex);
        }
    }

    private void validateUserAccess(Role role, SysUserEntity systemUser, SysUserEntity entity) {
        if (role != Role.ADMIN) {
            validateCompanyIdsAccess(systemUser.getCompanyIds(), entity.getCompanyIds().get(0));

            if (role == Role.USER && (systemUser.getId() != entity.getId()))
                throw new GenericForbiddenException();
        }
    }

    private void validateCompanyIdsAccess(List<UUID> companyIds, UUID companyId) {
        if (!companyIds.contains(companyId))
            throw new GenericForbiddenException();
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
