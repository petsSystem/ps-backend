package br.com.petshop.system.user.service;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericForbiddenException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.system.access.service.AccessGroupService;
import br.com.petshop.system.access.service.AccessGroupValidateService;
import br.com.petshop.system.user.model.dto.request.SysUserCreateRequest;
import br.com.petshop.system.user.model.dto.request.SysUserFilterRequest;
import br.com.petshop.system.user.model.dto.request.SysUserForgetRequest;
import br.com.petshop.system.user.model.dto.response.SysUserResponse;
import br.com.petshop.system.user.model.entity.SysUserEntity;
import br.com.petshop.system.user.model.enums.Message;
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
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SysUserValidateService {

    Logger log = LoggerFactory.getLogger(SysUserValidateService.class);
    @Autowired private SysUserService service;
    @Autowired private SysUserConverterService convert;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private AccessGroupValidateService accessGroupValidateService;

    public SysUserResponse create (Principal authentication, SysUserCreateRequest request) {
        try {

            Optional<SysUserEntity> userEntity = service.findByEmailAndActiveIsTrue(request.getEmail());

            if ((request.getRole() == Role.ADMIN || request.getRole() == Role.OWNER) &&
                    getRole(authentication) != Role.ADMIN)
                throw new GenericForbiddenException();

            if (userEntity.isPresent())
                throw new GenericAlreadyRegisteredException();

            if (request.getEmployeeId() == null) {
                if (getRole(authentication) != Role.ADMIN)
                    throw new GenericForbiddenException();
            }

            SysUserEntity entity = convert.createRequestIntoEntity(request);

            entity = service.create(entity, request.getEmployeeId());

            return convert.entityIntoResponse(entity);

        } catch (GenericAlreadyRegisteredException ex) {
            log.error(Message.USER_ALREADY_REGISTERED.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, Message.USER_ALREADY_REGISTERED.get(), ex);
        } catch (GenericNotFoundException ex) {
            log.error(Message.USER_EMPLOYEE_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.USER_EMPLOYEE_NOT_FOUND.get(), ex);
        } catch (GenericForbiddenException ex) {
            log.error(Message.USER_ERROR_FORBIDDEN.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, Message.USER_ERROR_FORBIDDEN.get(), ex);
        } catch (Exception ex) {
            log.error(Message.USER_ERROR_CREATE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_ERROR_CREATE.get(), ex);
        }
    }

    public void forget (Principal authentication, SysUserForgetRequest request) {
        try {

           service.forget(request.email());

        } catch (GenericNotFoundException ex) {
            log.error(Message.USER_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.USER_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.USER_ERROR_SENDING_PASSWORD.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_ERROR_SENDING_PASSWORD.get(), ex);
        }
    }

    public  List<SysUserResponse> partialUpdate(Principal authentication, UUID userId, JsonPatch patch) {
        try {

            JsonNode jsonPatchList = objectMapper.convertValue(patch, JsonNode.class);
            List<SysUserResponse> responses = new ArrayList<>();

            for(int i = 0; i < jsonPatchList.size(); i++) {
                validatePartialUpdateAccess(authentication, userId, jsonPatchList, i);

                SysUserEntity entity = service.partialUpdate(userId, patch);

                responses.add(convert.entityIntoResponse(entity));
            }

            return responses;

        } catch (GenericForbiddenException ex) {
            log.error(Message.USER_ERROR_FORBIDDEN.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.USER_ERROR_FORBIDDEN.get(), ex);

        } catch (GenericNotFoundException ex) {
            log.error(Message.USER_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.USER_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.USER_ERROR_PARTIAL.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_ERROR_PARTIAL.get(), ex);
        }
    }

    private void validatePartialUpdateAccess(Principal authentication,  UUID userId, JsonNode jsonPatchList, Integer i) {
        if (getRole(authentication) != Role.ADMIN) { //admin pode fazer qq coisa

            if (userId != getAuthUser(authentication).getId())
                throw new GenericForbiddenException();

            String op = jsonPatchList.get(i).get("op").toString();
            String path = jsonPatchList.get(i).get("path").toString();

            if (!op.equalsIgnoreCase("add") ||
                    !op.equalsIgnoreCase("replace"))
                throw new GenericForbiddenException();

            if (!path.equalsIgnoreCase("password"))
                throw new GenericForbiddenException();
        }
    }

    public Page<SysUserResponse> get(Principal authentication, Pageable pageable, SysUserFilterRequest filter) {
        try {

            if (getRole(authentication) == Role.USER)
                filter.setEmail(getAuthUser(authentication).getEmail());
            else if (getRole(authentication) == Role.OWNER || getRole(authentication) == Role.MANAGER)
                filter.setCompanyIds(getAuthUser(authentication).getEmployee().getCompanyIds());

            Page<SysUserEntity> entities = service.get(pageable, filter);

            List<SysUserResponse> response = entities.stream()
                    .map(c -> setAccessGroupInfo(authentication, convert.entityIntoResponse(c)))
                    .collect(Collectors.toList());

            return new PageImpl<>(response);

       } catch (Exception ex) {
            log.error(Message.USER_ERROR_GET.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_ERROR_GET.get(), ex);
        }
    }

    public SysUserResponse getById(Principal authentication, UUID userId) {
        try {

            SysUserEntity entity = null;
            if (getRole(authentication) == Role.ADMIN)
                entity = service.findById(userId);

            else {
                entity =  service.findByIdAndActiveIsTrue(userId);

                validateUserAccess(getAuthUser(authentication), entity);
            }

            return setAccessGroupInfo(authentication, convert.entityIntoResponse(entity));

        } catch (GenericNotFoundException ex) {
            log.error(Message.USER_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.USER_NOT_FOUND.get(), ex);
        } catch (GenericForbiddenException ex) {
            log.error(Message.USER_ERROR_FORBIDDEN.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, Message.USER_ERROR_FORBIDDEN.get(), ex);
        } catch (Exception ex) {
            log.error(Message.USER_ERROR_GET.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_ERROR_GET.get(), ex);
        }
    }

    private void validateUserAccess(SysUserEntity systemUser, SysUserEntity entity) {
        List<UUID> systemUserCompanies = systemUser.getEmployee().getCompanyIds();
        List<UUID> entityCompanies = entity.getEmployee().getCompanyIds();

        Optional<UUID> match = systemUserCompanies.stream()
                .filter(l -> entityCompanies.contains(l)).findFirst();

        if (match.isEmpty())
                throw new GenericForbiddenException();
    }

    private SysUserResponse setAccessGroupInfo(Principal authentication, SysUserResponse response) {
        if (response.getAccessGroupIds() != null) {
            response.setAccessGroups(response.getAccessGroupIds().stream()
                    .map(a -> accessGroupValidateService.getById(authentication, a))
                    .collect(Collectors.toList()));
        }
        return response;
    }

    public SysUserResponse getProfile(Principal authentication) {
        try {

            SysUserEntity entity = service.findByIdAndActiveIsTrue(getAuthUser(authentication).getId());

            return setAccessGroupInfo(authentication, convert.entityIntoResponse(entity));

        } catch (GenericNotFoundException ex) {
            log.error(Message.USER_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.USER_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.USER_ERROR_GET.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_ERROR_GET.get(), ex);
        }
    }

    public void delete(Principal authentication, UUID userId) {
        try {

            service.delete(userId);

        } catch (GenericNotFoundException ex) {
            log.error(Message.USER_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.USER_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.USER_ERROR_DELETE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_ERROR_DELETE.get(), ex);
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
