package br.com.petshop.system.user.service;

import br.com.petshop.authentication.service.JwtService;
import br.com.petshop.exception.EmailTokenException;
import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.system.user.model.dto.request.SysChangePasswordRequest;
import br.com.petshop.system.user.model.dto.request.SysEmailValidateRequest;
import br.com.petshop.system.user.model.dto.request.SysUserCreateRequest;
import br.com.petshop.system.user.model.entity.SysUserEntity;
import br.com.petshop.system.user.model.dto.request.SysUserUpdateRequest;
import br.com.petshop.system.user.model.dto.response.SysUserResponse;
import br.com.petshop.system.user.repository.SysUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SysUserService {

    Logger log = LoggerFactory.getLogger(SysUserService.class);
    @Autowired private SysUserRepository systemUserRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtService jwtService;
    @Autowired private SysUserConverterService convert;
    @Autowired private SysUserAsyncService asyncService;

    public SysUserResponse create (SysUserCreateRequest request) {
        try {
            SysUserEntity userEntity = systemUserRepository.findByEmailAndActiveIsTrue(request.getEmail()).orElse(null);

            if (userEntity != null)
                throw new GenericAlreadyRegisteredException("Email já cadastrado.");

            request.setPassword(passwordEncoder.encode(request.getPassword()));
            UserDetails user = convert.createRequestIntoEntity(request);
            userEntity = (SysUserEntity) user;
            userEntity.setEmailValidated(false);

            sendEmailToken(userEntity);

            SysUserResponse response = convert.entityIntoResponse(userEntity);
            response.setToken(jwtService.generateToken(user));

            return response;
        } catch (GenericAlreadyRegisteredException ex) {
            log.error("Already Registered: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao cadastrar usuário. Tente novamente mais tarde.", ex);
        }
    }

    private void sendEmailToken (SysUserEntity userEntity) {
        userEntity.setEmailToken(generateEmailTokenValidate());
        userEntity.setEmailTokenTime(LocalDateTime.now());

        asyncService.emailValidate(userEntity);

        save(userEntity);
    }

    private String generateEmailTokenValidate () {
        int number = (int) (1000 + Math.random() * (9999 - 1000 + 1));
        return String.valueOf(number);
    }

    public SysUserResponse emailValidate (Principal authentication, SysEmailValidateRequest request) {
        try {
            SysUserEntity userEntity = findByEmail(authentication.getName());
            if (request.emailToken().equalsIgnoreCase(userEntity.getEmailToken()) &&
                    !emailTokenExpired(userEntity.getEmailTokenTime())) {
                userEntity.setEmailValidated(true);

                save(userEntity);

                return convert.entityIntoResponse(userEntity);
            } else
                throw new EmailTokenException("Token inválido.");

        } catch (EmailTokenException ex) {
            log.error("Token exception: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao validar email. Tente novamente mais tarde.", ex);
        }
    }

    private Boolean emailTokenExpired (LocalDateTime emailTokenTime) {
        if (LocalDateTime.now().isAfter(emailTokenTime.plusMinutes(1)))
            throw new EmailTokenException("Token expirado. Solicite novo token.");
        return false;
    }

    public SysUserResponse emailValidateResend (Principal authentication) {
        try {
            SysUserEntity userEntity = findByEmail(authentication.getName());

            sendEmailToken(userEntity);

            return convert.entityIntoResponse(userEntity);

        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao reenviar email de validação. Tente novamente mais tarde.", ex);
        }
    }

    public void forget (String email) {
        try {
            SysUserEntity userEntity = findByEmail(email);

            String newPassword = generatePassword();
            userEntity.setPassword(passwordEncoder.encode(newPassword));
            userEntity.setChangePassword(true);

            save(userEntity);

            asyncService.forget(userEntity, newPassword);

        } catch (GenericNotFoundException ex) {
            log.error("Email not found: " + email);
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Email não cadastrado.", ex);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao enviar email. Tente novamente mais tarde.", ex);
        }
    }

    private String generatePassword () {
        String newPassword = UUID.randomUUID().toString();
        return newPassword.substring(0,6);
    }

    public void changePassword (Principal authentication, SysChangePasswordRequest request) {
        try {
            SysUserEntity userEntity = findByEmail(authentication.getName());
            userEntity.setPassword(passwordEncoder.encode(request.password()));
            userEntity.setChangePassword(false);
            save(userEntity);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao trocar senha. Tente novamente mais tarde.", ex);
        }
    }
    public SysUserResponse update (Principal authentication, SysUserUpdateRequest request) {
        try {
            SysUserEntity userEntity = findByEmail(authentication.getName());
            SysUserEntity newEntity = convert.updateRequestIntoEntity(request, userEntity);
            if (request.getPassword() != null)
                newEntity.setPassword(passwordEncoder.encode(request.getPassword()));
            save(newEntity);
            return convert.entityIntoResponse(newEntity);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao atualizar dados do usuário. Tente novamente mais tarde.", ex);
        }
    }

    public SysUserEntity findByEmail (String email) {
        return systemUserRepository.findByEmailAndActiveIsTrue(email)
                .orElseThrow(GenericNotFoundException::new);
    }

    public SysUserResponse getByEmail (Principal authentication) {
        try {
            SysUserEntity userEntity = findByEmail(authentication.getName());
            if (!userEntity.getEmailValidated())
                sendEmailToken(userEntity);

            SysUserResponse response = convert.entityIntoResponse(userEntity);
            return response;

        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao retornar dados do usuário. Tente novamente mais tarde.", ex);
        }
    }

    public SysUserEntity save (SysUserEntity entity) {
        return systemUserRepository.save(entity);
    }

    public void deactivate(String email) {
        try {
            SysUserEntity userEntity = findByEmail(email);
            userEntity.setActive(false);
            save(userEntity);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao retornar dados do usuário. Tente novamente mais tarde.", ex);
        }
    }
}