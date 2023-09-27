package br.com.petshop.app.user.service;

import br.com.petshop.app.address.model.dto.response.AppAddressResponse;
import br.com.petshop.app.address.service.AppAddressService;
import br.com.petshop.app.user.model.dto.request.AppUserForgetRequest;
import br.com.petshop.app.user.model.dto.request.AppUserUpdateRequest;
import br.com.petshop.app.user.model.dto.response.AppUserResponse;
import br.com.petshop.app.user.model.enums.Message;
import br.com.petshop.authentication.service.JwtService;
import br.com.petshop.exception.EmailTokenException;
import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.app.pet.model.dto.response.PetResponse;
import br.com.petshop.app.pet.service.PetService;
import br.com.petshop.app.user.model.dto.request.AppUserCreateRequest;
import br.com.petshop.app.user.model.dto.request.ChangePasswordRequest;
import br.com.petshop.app.user.model.dto.request.EmailValidateRequest;
import br.com.petshop.app.user.model.entity.AppUserEntity;
import br.com.petshop.app.user.repository.AppUserRepository;
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
import java.util.Set;
import java.util.UUID;

@Service
public class
AppUserService {

    Logger log = LoggerFactory.getLogger(AppUserService.class);
    @Autowired private AppUserRepository appUserRepository;
    @Autowired private AppAddressService addressService;
    @Autowired private PetService petService;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtService jwtService;
    @Autowired private AppUserConverterService convert;
    @Autowired private AppUserAsyncService asyncService;

    public AppUserResponse create(AppUserCreateRequest request) {
        try {
            AppUserEntity userEntity = appUserRepository.findByEmailAndActiveIsTrue(request.getEmail()).orElse(null);

            if (userEntity != null)
                throw new GenericAlreadyRegisteredException(Message.USER_ALREADY_REGISTERED.get());

            request.setPassword(passwordEncoder.encode(request.getPassword()));
            UserDetails user = convert.appUserCreateRequestIntoEntity(request);
            userEntity = (AppUserEntity) user;
            userEntity.setEmailValidated(false);

            sendEmailToken(userEntity);

            AppUserResponse response = convert.appUserEntityIntoResponse(userEntity);
            response.setToken(jwtService.generateToken(user));

            return response;
        } catch (GenericAlreadyRegisteredException ex) {
            log.error("Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error(Message.USER_ERROR_CREATE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_ERROR_CREATE.get(), ex);
        }
    }

    private void sendEmailToken(AppUserEntity userEntity) {
        userEntity.setEmailToken(generateEmailTokenValidate());
        userEntity.setEmailTokenTime(LocalDateTime.now());

        asyncService.emailValidate(userEntity);

        save(userEntity);
    }

    private String generateEmailTokenValidate() {
        int number = (int) (1000 + Math.random() * (9999 - 1000 + 1));
        return String.valueOf(number);
    }

    public AppUserResponse emailValidate(Principal authentication, EmailValidateRequest request) {
        try {
            AppUserEntity userEntity = findByEmail(authentication.getName());
            if (request.emailToken().equalsIgnoreCase(userEntity.getEmailToken()) &&
                    !emailTokenExpired(userEntity.getEmailTokenTime())) {
                userEntity.setEmailValidated(true);

                save(userEntity);

                return convert.appUserEntityIntoResponse(userEntity);
            } else
                throw new EmailTokenException(Message.USER_ERROR_INVALID_TOKEN.get());

        } catch (EmailTokenException ex) {
            log.error("Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error(Message.USER_ERROR_VALIDATE_EMAIL.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_ERROR_VALIDATE_EMAIL.get(), ex);
        }
    }

    private Boolean emailTokenExpired(LocalDateTime emailTokenTime) {
        if (LocalDateTime.now().isAfter(emailTokenTime.plusMinutes(1)))
            throw new EmailTokenException(Message.USER_ERROR_EXPIRED_TOKEN.get());
        return false;
    }

    public AppUserResponse emailValidateResend(Principal authentication) {
        try {
            AppUserEntity userEntity = findByEmail(authentication.getName());

            sendEmailToken(userEntity);

            return convert.appUserEntityIntoResponse(userEntity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.USER_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.USER_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.USER_ERROR_RESEND_TOKEN.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_ERROR_RESEND_TOKEN.get(), ex);
        }
    }

    public void forget(AppUserForgetRequest request) {
        try {
            AppUserEntity userEntity = findByEmail(request.getEmail());

            String newPassword = generatePassword();
            userEntity.setPassword(passwordEncoder.encode(newPassword));
            userEntity.setChangePassword(request.getChangePassword());

            save(userEntity);

            asyncService.forget(userEntity, newPassword);

        } catch (GenericNotFoundException ex) {
            log.error(Message.USER_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.USER_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.USER_ERROR_SEND_EMAIL.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_ERROR_SEND_EMAIL.get(), ex);
        }
    }

    private String generatePassword() {
        String newPassword = UUID.randomUUID().toString();
        return newPassword.substring(0,6);
    }

    public void changePassword(Principal authentication, ChangePasswordRequest request) {
        try {
            AppUserEntity userEntity = findByEmail(authentication.getName());
            userEntity.setPassword(passwordEncoder.encode(request.password()));
            userEntity.setChangePassword(false);
            save(userEntity);
        } catch (GenericNotFoundException ex) {
            log.error(Message.USER_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.USER_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.USER_ERROR_CHANGE_PASSWORD.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_ERROR_CHANGE_PASSWORD.get(), ex);
        }
    }
    public AppUserResponse update(Principal authentication, AppUserUpdateRequest request) {
        try {
            AppUserEntity userEntity = findByEmail(authentication.getName());
            AppUserEntity newEntity = convert.appUserUpdateRequestIntoEntity(request, userEntity);
            if (request.getPassword() != null)
                newEntity.setPassword(passwordEncoder.encode(request.getPassword()));
            save(newEntity);
            return convert.appUserEntityIntoResponse(newEntity);
        } catch (GenericNotFoundException ex) {
            log.error(Message.USER_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.USER_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.USER_ERROR_UPDATE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.USER_ERROR_UPDATE.get(), ex);
        }
    }

    public AppUserEntity findByEmail(String email) {
        return appUserRepository.findByEmailAndActiveIsTrue(email)
                .orElseThrow(GenericNotFoundException::new);
    }

    public AppUserResponse getByEmail(Principal authentication) {
        try {
            AppUserEntity userEntity = findByEmail(authentication.getName());
            if (!userEntity.getEmailValidated())
                sendEmailToken(userEntity);

            AppUserResponse response = convert.appUserEntityIntoResponse(userEntity);

            Set<AppAddressResponse> addressResponse = addressService.get(authentication);
            if (!addressResponse.isEmpty())
                response.setAddresses(addressResponse);

            Set<PetResponse> petResponse = petService.get(authentication);
            if (!petResponse.isEmpty())
                response.setPets(petResponse);

            return response;
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

    public AppUserEntity save (AppUserEntity entity) {
        return appUserRepository.save(entity);
    }

    public void deactivate(String email) {
        try {
            AppUserEntity userEntity = findByEmail(email);
            userEntity.setActive(false);
            save(userEntity);
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
