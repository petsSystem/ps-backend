package br.com.petshop.user.service;

import br.com.petshop.authentication.service.JwtService;
import br.com.petshop.dao.entity.AddressEntity;
import br.com.petshop.dao.entity.AppUserEntity;
import br.com.petshop.dao.repository.AppUserRepository;
import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.model.dto.request.AddressRequest;
import br.com.petshop.model.dto.request.AppUserCreateRequest;
import br.com.petshop.model.dto.request.AppUserUpdateRequest;
import br.com.petshop.model.dto.request.ChangePasswordRequest;
import br.com.petshop.model.dto.response.AppUserResponse;
import br.com.petshop.notification.MailNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
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
    @Autowired private MailNotificationService mailNotificationService;
    public AppUserResponse create(AppUserCreateRequest request) {
        try {
            AppUserEntity userEntity = appUserRepository.findByEmail(request.getEmail()).orElse(null);

            if (userEntity != null)
                throw new GenericAlreadyRegisteredException("Email já cadastrado.");

            request.setPassword(passwordEncoder.encode(request.getPassword()));
            UserDetails user = convert.convertAppUserCreateRequestIntoEntity(request);
            userEntity = (AppUserEntity) user;
            save(userEntity);

            AppUserResponse response = convert.convertAppUserEntityIntoResponse(userEntity);
            response.setToken(jwtService.generateToken(user));

            return response;
        } catch (GenericAlreadyRegisteredException ex) {
            log.error("Already Registered: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), ex);
        }catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao cadastrar usuário. Tente novamente mais tarde.", ex);
        }
    }

    public void checkForget(String email) {
        try {
            AppUserEntity userEntity = findByEmail(email);
            forget(userEntity);
            return;
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

    @Async
    public void forget(AppUserEntity userEntity) {
        String newPassword = generatePassword();
        userEntity.setPassword(passwordEncoder.encode(newPassword));
        userEntity.setChangePassword(true);

        save(userEntity);

        mailNotificationService.send(userEntity.getEmail(), newPassword);
    }

    private String generatePassword() {
        String newPassword = UUID.randomUUID().toString();
        return newPassword.substring(0,5);
    }

    public void changePassword(Principal authentication, ChangePasswordRequest request) {
        try {
            AppUserEntity userEntity = findByEmail(authentication.getName());
            //AppUserEntity)((UsernamePasswordAuthenticationToken)authentication).getPrincipal();
            userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
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
            save(newEntity);
            return convert.convertAppUserEntityIntoResponse(newEntity);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao atualizar dados do usuário. Tente novamente mais tarde.", ex);
        }
    }

    public AppUserResponse address(Principal authentication, AddressRequest request) {
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
    public AppUserEntity findByEmail(String mail) {
        return appUserRepository.findByEmail(mail)
                .orElseThrow(GenericNotFoundException::new);
    }

    public AppUserEntity save (AppUserEntity entity) {
        return appUserRepository.save(entity);
    }
}
