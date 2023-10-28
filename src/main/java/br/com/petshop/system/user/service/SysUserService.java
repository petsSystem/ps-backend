package br.com.petshop.system.user.service;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericForbiddenException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.system.employee.model.dto.response.EmployeeResponse;
import br.com.petshop.system.employee.model.entity.EmployeeEntity;
import br.com.petshop.system.employee.service.EmployeeService;
import br.com.petshop.system.user.model.dto.request.SysUserCreateRequest;
import br.com.petshop.system.user.model.dto.request.SysUserFilterRequest;
import br.com.petshop.system.user.model.dto.request.SysUserForgetRequest;
import br.com.petshop.system.user.model.dto.response.SysUserResponse;
import br.com.petshop.system.user.model.entity.SysUserEntity;
import br.com.petshop.system.user.model.enums.Message;
import br.com.petshop.system.user.repository.SysUserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

@Service
public class SysUserService {

    Logger log = LoggerFactory.getLogger(SysUserService.class);
    @Autowired private SysUserRepository systemUserRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private SysUserConverterService convert;
    @Autowired private SysUserAsyncService asyncService;
    @Autowired private EmployeeService employeeService;
    @Autowired private ObjectMapper objectMapper;

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
                throw new GenericForbiddenException("Acesso negado.");

            String op = jsonPatchList.get(i).get("op").toString();
            String path = jsonPatchList.get(i).get("path").toString();

            if (!op.equalsIgnoreCase("add") ||
                    !op.equalsIgnoreCase("replace"))
                throw new GenericForbiddenException("Acesso negado.");

            if (!path.equalsIgnoreCase("password"))
                throw new GenericForbiddenException("Acesso negado.");
        }
    }

    private SysUserEntity applyPatch(JsonPatch patch, SysUserEntity entity) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        return objectMapper.treeToValue(patched, SysUserEntity.class);
    }

    public Page<EmployeeResponse> get(Principal authentication, Pageable pageable, SysUserFilterRequest filter) {
        return null;
    }

    public void delete(UUID userId) {
    }


//    private void sendEmailToken (SysUserEntity userEntity) {
//        userEntity.setEmailToken(generateEmailTokenValidate());
//        userEntity.setEmailTokenTime(LocalDateTime.now());
//
//        asyncService.emailValidate(userEntity);
//
//        save(userEntity);
//    }
//
//    private String generateEmailTokenValidate () {
//        int number = (int) (1000 + Math.random() * (9999 - 1000 + 1));
//        return String.valueOf(number);
//    }
//
//    public SysUserResponse emailValidate (Principal authentication, SysEmailValidateRequest request) {
//        try {
//            SysUserEntity userEntity = findByEmail(authentication.getName());
//            if (request.emailToken().equalsIgnoreCase(userEntity.getEmailToken()) &&
//                    !emailTokenExpired(userEntity.getEmailTokenTime())) {
//                userEntity.setEmailValidated(true);
//
//                save(userEntity);
//
//                return convert.entityIntoResponse(userEntity);
//            } else
//                throw new EmailTokenException("Token inválido.");
//
//        } catch (EmailTokenException ex) {
//            log.error("Token exception: " + ex.getMessage());
//            throw new ResponseStatusException(
//                    HttpStatus.UNAUTHORIZED, ex.getMessage(), ex);
//        } catch (Exception ex) {
//            log.error("Bad Request: " + ex.getMessage());
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST, "Erro ao validar email. Tente novamente mais tarde.", ex);
//        }
//    }
//
//    private Boolean emailTokenExpired (LocalDateTime emailTokenTime) {
//        if (LocalDateTime.now().isAfter(emailTokenTime.plusMinutes(1)))
//            throw new EmailTokenException("Token expirado. Solicite novo token.");
//        return false;
//    }
//
//    public SysUserResponse emailValidateResend (Principal authentication) {
//        try {
//            SysUserEntity userEntity = findByEmail(authentication.getName());
//
//            sendEmailToken(userEntity);
//
//            return convert.entityIntoResponse(userEntity);
//
//        } catch (Exception ex) {
//            log.error("Bad Request: " + ex.getMessage());
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST, "Erro ao reenviar email de validação. Tente novamente mais tarde.", ex);
//        }
//    }
//
//
//
//
//
//    public void changePassword (Principal authentication, SysChangePasswordRequest request) {
//        try {
//            SysUserEntity userEntity = findByEmail(authentication.getName());
//            userEntity.setPassword(passwordEncoder.encode(request.password()));
//            userEntity.setChangePassword(false);
//            save(userEntity);
//        } catch (Exception ex) {
//            log.error("Bad Request: " + ex.getMessage());
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST, "Erro ao trocar senha. Tente novamente mais tarde.", ex);
//        }
//    }
//    public SysUserResponse update (Principal authentication, SysUserUpdateRequest request) {
//        try {
//            SysUserEntity userEntity = findByEmail(authentication.getName());
//            SysUserEntity newEntity = convert.updateRequestIntoEntity(request, userEntity);
//            if (request.getPassword() != null)
//                newEntity.setPassword(passwordEncoder.encode(request.getPassword()));
//            save(newEntity);
//            return convert.entityIntoResponse(newEntity);
//        } catch (Exception ex) {
//            log.error("Bad Request: " + ex.getMessage());
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST, "Erro ao atualizar dados do usuário. Tente novamente mais tarde.", ex);
//        }
//    }
//
//
//
//    public SysUserResponse getByEmail (Principal authentication) {
//        try {
//            SysUserEntity userEntity = findByEmail(authentication.getName());
//            if (!userEntity.getEmailValidated())
//                sendEmailToken(userEntity);
//
//            SysUserResponse response = convert.entityIntoResponse(userEntity);
//            return response;
//
//        } catch (Exception ex) {
//            log.error("Bad Request: " + ex.getMessage());
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST, "Erro ao retornar dados do usuário. Tente novamente mais tarde.", ex);
//        }
//    }
//
//    public SysUserEntity save (SysUserEntity entity) {
//        return systemUserRepository.save(entity);
//    }
//
//    public void deactivate(String email) {
//        try {
//            SysUserEntity userEntity = findByEmail(email);
//            userEntity.setActive(false);
//            save(userEntity);
//        } catch (Exception ex) {
//            log.error("Bad Request: " + ex.getMessage());
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST, "Erro ao retornar dados do usuário. Tente novamente mais tarde.", ex);
//        }
//    }
}
