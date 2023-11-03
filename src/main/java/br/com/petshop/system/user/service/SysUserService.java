package br.com.petshop.system.user.service;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericForbiddenException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.system.employee.model.entity.EmployeeEntity;
import br.com.petshop.system.employee.service.EmployeeService;
import br.com.petshop.system.user.model.dto.request.SysUserCreateRequest;
import br.com.petshop.system.user.model.dto.request.SysUserFilterRequest;
import br.com.petshop.system.user.model.dto.request.SysUserForgetRequest;
import br.com.petshop.system.user.model.dto.response.SysUserResponse;
import br.com.petshop.system.user.model.entity.SysUserEntity;
import br.com.petshop.system.user.model.enums.Message;
import br.com.petshop.system.user.repository.SysUserRepository;
import br.com.petshop.system.user.repository.SysUserSpecification;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SysUserService {

    Logger log = LoggerFactory.getLogger(SysUserService.class);
    @Autowired private SysUserRepository systemUserRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private SysUserConverterService convert;
    @Autowired private SysUserAsyncService asyncService;
    @Autowired private EmployeeService employeeService;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private SysUserSpecification specification;

    public SysUserResponse create (Principal authentication, SysUserCreateRequest request) {
        try {
            createValidate(authentication, request);

            EmployeeEntity employeeEntity = employeeService.findByIdAndActive(request.getEmployeeId());
            request.setEmail(employeeEntity.getEmail());

            SysUserEntity entity = convert.createRequestIntoEntity(request);
            String password = generatePassword();

            entity.setPassword(passwordEncoder.encode(password));
            entity.setEmployee(employeeEntity);
            entity = systemUserRepository.save(entity);

            asyncService.sendNewPassword(entity, password);

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

    private void createValidate(Principal authentication, SysUserCreateRequest request) {
        SysUserEntity systemUser = ((SysUserEntity) ((UsernamePasswordAuthenticationToken)
                authentication).getPrincipal());

        Optional<SysUserEntity> userEntity = systemUserRepository.findByEmailAndActiveIsTrue(request.getEmail());

        if ((request.getRole() == Role.ADMIN || request.getRole() == Role.OWNER) && systemUser.getRole() != Role.ADMIN)
            throw new GenericForbiddenException();

        if (userEntity.isPresent())
            throw new GenericAlreadyRegisteredException();

        if (request.getEmployeeId() == null) {
            if (systemUser.getRole() != Role.ADMIN)
                throw new GenericForbiddenException();
        }
    }

    private String generatePassword () {
        String newPassword = UUID.randomUUID().toString();
        return newPassword.substring(0,8);
    }

    public void forget (SysUserForgetRequest request) {
        try {
            SysUserEntity entity = systemUserRepository.findByEmailAndActiveIsTrue(request.email())
                    .orElseThrow(GenericNotFoundException::new);

            String newPassword = generatePassword();
            entity.setPassword(passwordEncoder.encode(newPassword));
            entity.setChangePassword(true);

            entity = systemUserRepository.save(entity);

            asyncService.sendNewPassword(entity, newPassword);

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
            SysUserEntity systemUser = ((SysUserEntity) ((UsernamePasswordAuthenticationToken)
                    authentication).getPrincipal());
            JsonNode jsonPatchList = objectMapper.convertValue(patch, JsonNode.class);
            List<SysUserResponse> responses = new ArrayList<>();
            for(int i = 0; i < jsonPatchList.size(); i++) {
                validatePartialUpdateAccess(systemUser, userId, jsonPatchList, i);

                SysUserEntity entity = systemUserRepository.findByIdAndActiveIsTrue(userId)
                        .orElseThrow(GenericNotFoundException::new);

                entity = applyPatch(patch, entity);
                entity = systemUserRepository.save(entity);

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

    private void validatePartialUpdateAccess(SysUserEntity systemUser, UUID userId, JsonNode jsonPatchList, Integer i) {
        if (systemUser.getRole() != Role.ADMIN) { //admin pode fazer qq coisa

            if (userId != systemUser.getId())
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

    private SysUserEntity applyPatch(JsonPatch patch, SysUserEntity entity) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        return objectMapper.treeToValue(patched, SysUserEntity.class);
    }

    public Page<SysUserResponse> get(Principal authentication, Pageable pageable, SysUserFilterRequest filter) {
        try {
            SysUserEntity systemUser = ((SysUserEntity) ((UsernamePasswordAuthenticationToken)
                    authentication).getPrincipal());

            if (systemUser.getRole() == Role.USER)
                filter.setEmail(systemUser.getEmail());
            else if (systemUser.getRole() == Role.OWNER || systemUser.getRole() == Role.MANAGER)
                filter.setCompanyIds(systemUser.getEmployee().getCompanyIds());

            return get(pageable, filter);

       } catch (Exception ex) {
            log.error(Message.USER_ERROR_GET.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_ERROR_GET.get(), ex);
        }
    }

    private  Page<SysUserResponse> get(Pageable pageable, SysUserFilterRequest filter) {
        Specification<SysUserEntity> filters = specification.filter(filter);

        Page<SysUserEntity> entities = systemUserRepository.findAll(filters, pageable);

        List<SysUserResponse> response = entities.stream()
                .map(c -> convert.entityIntoResponse(c))
                .collect(Collectors.toList());

        return new PageImpl<>(response);
    }

    public SysUserResponse getById(Principal authentication, UUID userId) {
        try {
            SysUserEntity systemUser = ((SysUserEntity) ((UsernamePasswordAuthenticationToken)
                    authentication).getPrincipal());

            if (systemUser.getRole() == Role.ADMIN) {
                SysUserEntity entity = systemUserRepository.findById(userId).orElseThrow(GenericNotFoundException::new);
                return convert.entityIntoResponse(entity);
            }

            SysUserEntity entity = systemUserRepository.findByIdAndActiveIsTrue(userId)
                    .orElseThrow(GenericNotFoundException::new);

            validateUserAccess(systemUser, entity);

            return convert.entityIntoResponse(entity);

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

        //FAZER FOR PARA VERIFICAR LIST DE LIST SE CONTAINS


        if (systemUser.getRole() != Role.ADMIN) {
            if (!systemUser.getEmployee().getCompanyIds().contains(entity.getEmployee().getCompanyIds().get(0)))
                throw new GenericForbiddenException();
        }
    }

    public void delete(UUID userId) {
        try {
            SysUserEntity entity = systemUserRepository.findById(userId)
                    .orElseThrow(GenericNotFoundException::new);
            systemUserRepository.delete(entity);

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
}
