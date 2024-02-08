package br.com.petshop.user.service;

import br.com.petshop.commons.exception.GenericAlreadyRegisteredException;
import br.com.petshop.commons.exception.GenericForbiddenException;
import br.com.petshop.commons.exception.GenericIncorrectPasswordException;
import br.com.petshop.commons.exception.GenericNotActiveException;
import br.com.petshop.commons.exception.GenericNotFoundException;
import br.com.petshop.commons.service.AuthenticationCommonService;
import br.com.petshop.company.model.entity.CompanyEntity;
import br.com.petshop.profile.model.dto.Permission;
import br.com.petshop.profile.model.dto.response.ProfileResponse;
import br.com.petshop.user.model.dto.request.SysUserCreateRequest;
import br.com.petshop.user.model.dto.request.SysUserPasswordRequest;
import br.com.petshop.user.model.dto.request.SysUserUpdateRequest;
import br.com.petshop.user.model.dto.response.SysUserMeResponse;
import br.com.petshop.user.model.dto.response.SysUserProfileResponse;
import br.com.petshop.user.model.dto.response.SysUserResponse;
import br.com.petshop.user.model.dto.response.SysUserTableResponse;
import br.com.petshop.user.model.entity.UserEntity;
import br.com.petshop.user.model.enums.Message;
import com.github.fge.jsonpatch.JsonPatch;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SysUserFacadeService extends AuthenticationCommonService {
    private Logger log = LoggerFactory.getLogger(SysUserFacadeService.class);
    @Autowired private SysUserValidationService validate;
    @Autowired private SysUserService service;
    @Autowired private SysUserConverterService converter;

    public SysUserResponse create(Principal authentication, SysUserCreateRequest request) {
        try {
            validate.accessByCompany(authentication, request.getCompanyIds());

            UserEntity entity = converter.createRequestIntoEntity(request);
            entity = service.create(entity);

            List<ProfileResponse> profiles = service.getProfiles(entity);

            SysUserResponse response = converter.entityIntoResponse(entity);
            response.setProfiles(profiles);

            return response;

        } catch (GenericForbiddenException ex) {
            log.error(Message.USER_FORBIDDEN_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, Message.USER_FORBIDDEN_ERROR.get(), ex);
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

    public void forget (String username) {
        try {
            service.forget(username);

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

    public SysUserResponse changePassword(Principal authentication, SysUserPasswordRequest request) {
        try {
            UserEntity user = getSysAuthUser(authentication);
            UserEntity entity = service.changePassword(user.getId(), request);

            return converter.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.USER_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.USER_NOT_FOUND_ERROR.get(), ex);
        } catch (GenericIncorrectPasswordException ex) {
            log.error(Message.USER_OLD_PASSWORD_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.USER_OLD_PASSWORD_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.USER_CHANGING_PASSWORD_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_CHANGING_PASSWORD_ERROR.get(), ex);
        }
    }

    public SysUserProfileResponse getProfile(Principal authentication) {
        try {
            UserEntity entity = getSysAuthUser(authentication);
            entity = service.findById(entity.getId());

            return converter.entityIntoProfileResponse(entity);

        } catch (Exception ex) {
            log.error(Message.USER_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_GET_ERROR.get(), ex);
        }
    }

    public SysUserMeResponse getMeInfo(Principal authentication) {
        try {
            UserEntity entity = getSysAuthUser(authentication);
            entity = service.findById(entity.getId());

            CompanyEntity companyEntity = service.getCompanyInfo(entity);

            List<ProfileResponse> profiles = service.getProfiles(entity);
            List<Permission> permissions = profiles.stream()
                    .flatMap(p -> p.getPermissions().stream())
                    .collect(Collectors.toList());

            SysUserMeResponse response = converter.entityIntoMeResponse(entity);
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
            UserEntity entity = service.findById(userId);

            validate.accessByCompany(authentication, entity.getCompanyIds());
            validate.activate(authentication, entity);

            entity = service.active(entity, patch);

            List<ProfileResponse> profiles = service.getProfiles(entity);

            SysUserResponse response = converter.entityIntoResponse(entity);
            response.setProfiles(profiles);

            return response;

        } catch (GenericForbiddenException ex) {
            log.error(Message.USER_FORBIDDEN_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, Message.USER_FORBIDDEN_ERROR.get(), ex);
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

    public SysUserMeResponse updateCurrentCompany(Principal authentication, JsonPatch patch) {
        try {
            UserEntity user = getSysAuthUser(authentication);

            UserEntity entity = service.updateCurrentCompany(user, patch);

            CompanyEntity companyEntity = service.getCompanyInfo(entity);

            List<ProfileResponse> profiles = service.getProfiles(entity);
            List<Permission> permissions = profiles.stream()
                    .flatMap(p -> p.getPermissions().stream())
                    .collect(Collectors.toList());

            SysUserMeResponse response = converter.entityIntoMeResponse(entity);
            response.setPermissions(permissions);
            response.setCompanyId(companyEntity.getId());
            response.setCompanyName(companyEntity.getName());

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
            UserEntity entity = service.findByIdAndActiveIsTrue(userId);

            validate.accessByUser(authentication, entity);

            UserEntity entityRequest = converter.updateRequestIntoEntity(request);

            entity = service.updateById(entityRequest, entity);

            List<ProfileResponse> profiles = service.getProfiles(entity);

            SysUserResponse response = converter.entityIntoResponse(entity);
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

    public  Page<SysUserTableResponse> get(Principal authentication, UUID companyId, UUID productId, Pageable pageable) {
        try {
            validate.accessByCompany(authentication, companyId);

            Page<UserEntity> entities = service.findAllByFilter(companyId, productId, pageable);

            List<SysUserTableResponse> response = entities.stream()
                    .map(c -> converter.entityIntoTableResponse(c))
                    .collect(Collectors.toList());

            Collections.sort(response, Comparator.comparing(SysUserTableResponse::getActive).reversed()
                    .thenComparing(SysUserTableResponse::getName));

            return new PageImpl<>(response);

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

    public SysUserResponse getById(Principal authentication, UUID userId) {
        try {
            UserEntity entity = service.findById(userId);

            validate.accessByUser(authentication, entity);

            List<ProfileResponse> profiles = service.getProfiles(entity);

            SysUserResponse response = converter.entityIntoResponse(entity);
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
}
