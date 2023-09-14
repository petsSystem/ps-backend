package br.com.petshop.user.app.service;

import br.com.petshop.authentication.service.JwtService;
import br.com.petshop.user.app.model.dto.request.AppUserUpdateRequest;
import br.com.petshop.user.app.model.dto.response.AppUserResponse;
import br.com.petshop.user.app.model.entity.AddressEntity;
import br.com.petshop.user.app.model.entity.AppUserEntity;
import br.com.petshop.user.app.repository.AppUserRepository;
import br.com.petshop.exception.EmailTokenException;
import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.user.app.model.dto.request.AddressRequest;
import br.com.petshop.user.app.model.dto.request.AppUserCreateRequest;
import br.com.petshop.user.app.model.dto.request.ChangePasswordRequest;
import br.com.petshop.user.app.model.dto.request.EmailValidateRequest;
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
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class AppUserService {

    Logger log = LoggerFactory.getLogger(AppUserService.class);
    @Autowired private AppUserRepository appUserRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtService jwtService;
    @Autowired private ConvertService convert;

    @Autowired private AsyncService asyncService;

    public AppUserResponse create(AppUserCreateRequest request) {
        try {
            AppUserEntity userEntity = appUserRepository.findByEmailAndActiveIsTrue(request.getEmail()).orElse(null);

            if (userEntity != null)
                throw new GenericAlreadyRegisteredException("Email já cadastrado.");

            request.setPassword(passwordEncoder.encode(request.getPassword()));
            UserDetails user = convert.convertAppUserCreateRequestIntoEntity(request);
            userEntity = (AppUserEntity) user;
            userEntity.setEmailValidated(false);

            sendEmailToken(userEntity);

            AppUserResponse response = convert.convertAppUserEntityIntoResponse(userEntity);
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

                return convert.convertAppUserEntityIntoResponse(userEntity);
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

    private Boolean emailTokenExpired(LocalDateTime emailTokenTime) {
        if (LocalDateTime.now().isAfter(emailTokenTime.plusMinutes(1)))
            throw new EmailTokenException("Token expirado. Solicite novo token.");
        return false;
    }

    public AppUserResponse emailValidateResend(Principal authentication) {
        try {
            AppUserEntity userEntity = findByEmail(authentication.getName());

            sendEmailToken(userEntity);

            return convert.convertAppUserEntityIntoResponse(userEntity);

        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao reenviar email de validação. Tente novamente mais tarde.", ex);
        }
    }

    public void forget(String email) {
        try {
            AppUserEntity userEntity = findByEmail(email);

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
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao trocar senha. Tente novamente mais tarde.", ex);
        }
    }
    public AppUserResponse update(Principal authentication, AppUserUpdateRequest request) {
        try {
            AppUserEntity userEntity = findByEmail(authentication.getName());
            AppUserEntity newEntity = convert.convertAppUserUpdateRequestIntoEntity(request, userEntity);
            if (request.getPassword() != null)
                newEntity.setPassword(passwordEncoder.encode(request.getPassword()));
            save(newEntity);
            return convert.convertAppUserEntityIntoResponse(newEntity);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao atualizar dados do usuário. Tente novamente mais tarde.", ex);
        }
    }

    public AppUserResponse createAddress(Principal authentication, AddressRequest request) {
        try {
            AppUserEntity userEntity = findByEmail(authentication.getName());
            AddressEntity addressEntity = convert.convertAddressRequestIntoEntity(request);
            Set<AddressEntity> entities = userEntity.getAppUserAddresses();
            if (entities == null)
                entities = new HashSet<>();

            entities.add(addressEntity);
            userEntity.setAppUserAddresses(entities);
            save(userEntity);
            return convert.convertAppUserEntityIntoResponse(userEntity);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao atualizar dados do endereço do usuário. Tente novamente mais tarde.", ex);
        }
    }

    public AppUserResponse deleteAddress(Principal authentication, String idAddress) {
        try {
            AppUserEntity userEntity = findByEmail(authentication.getName());

            Set<AddressEntity> addressEntities = userEntity.getAppUserAddresses();
            addressEntities.removeIf(a -> a.getId().equalsIgnoreCase(idAddress));
            userEntity.setAppUserAddresses(addressEntities);

            save(userEntity);
            return convert.convertAppUserEntityIntoResponse(userEntity);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao excluir endereço do cadastro. Tente novamente mais tarde.", ex);
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

            return convert.convertAppUserEntityIntoResponse(userEntity);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao retornar dados do usuário. Tente novamente mais tarde.", ex);
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
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao retornar dados do usuário. Tente novamente mais tarde.", ex);
        }
    }
}
