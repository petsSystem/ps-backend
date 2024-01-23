package br.com.petshop.customer.service;

import br.com.petshop.app.address.service.AppAddressService;
import br.com.petshop.customer.model.dto.request.AppUserUpdateRequest;
import br.com.petshop.customer.model.dto.request.CustomerChangePasswordRequest;
import br.com.petshop.customer.model.dto.request.EmailValidateRequest;
import br.com.petshop.customer.model.dto.response.CustomerResponse;
import br.com.petshop.customer.model.entity.CustomerEntity;
import br.com.petshop.customer.model.enums.AppStatus;
import br.com.petshop.customer.model.enums.Message;
import br.com.petshop.customer.model.enums.Origin;
import br.com.petshop.customer.repository.CustomerRepository;
import br.com.petshop.app.pet.service.PetService;
import br.com.petshop.authentication.service.JwtService;
import br.com.petshop.exception.EmailTokenException;
import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericIncorrectPasswordException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.notification.MailNotificationService;
import br.com.petshop.company.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class
CustomerService {
    private Logger log = LoggerFactory.getLogger(CustomerService.class);
    @Autowired private CustomerRepository repository;
    @Autowired private MailNotificationService mailNotificationService;
    @Autowired private AppAddressService addressService;
    @Autowired private PetService petService;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtService jwtService;
    @Autowired private CustomerConverterService convert;

    @Autowired private CompanyService companyService;

    @Autowired private GeometryService geometry;

    public void forget(String username) {
        CustomerEntity customerEntity = repository.findByUsernameAndActiveIsTrue(username)
                .orElseThrow(GenericNotFoundException::new);

        String newPassword = generatePassword();
        customerEntity.setPassword(passwordEncoder.encode(newPassword));
        customerEntity.setChangePassword(true);

        save(customerEntity);

        //DESENVOLVER HTML PARA ENVIO DE EMAIL

        String subject = "Nova senha - APP Pet System";
        String body = "Sua nova senha é: " + newPassword;

        mailNotificationService.send(customerEntity.getEmail(), subject, body);
    }

    public CustomerEntity create(CustomerEntity entity) {

        CustomerEntity customerEntity = repository.findByCpf(entity.getCpf()).orElse(null);

        if (customerEntity != null)
            if (customerEntity.getAppStatus() == AppStatus.ACTIVE)
                throw new GenericAlreadyRegisteredException();
            else {
                customerEntity.setAppStatus(AppStatus.ACTIVE);
                customerEntity.setPassword(passwordEncoder.encode(entity.getPassword()));
                sendValidationEmailToken(customerEntity);
                return save(customerEntity);
            }

        if (entity.getOrigin() == Origin.APP) {
            entity.setPassword(passwordEncoder.encode(entity.getPassword()));
            sendValidationEmailToken(entity);
        } else {
            sendEmailApp(entity);
        }

        entity.setUsername(entity.getCpf());

        return save(entity);
    }

    private void sendValidationEmailToken(CustomerEntity userEntity) {
        userEntity.setEmailToken(generateEmailTokenValidate());
        userEntity.setEmailTokenTime(LocalDateTime.now());

        //DESENVOLVER HTML PARA ENVIO DE EMAIL

        String subject = "Validação de email - APP Pet System";
        String body = "Seu token de validação é: " + userEntity.getEmailToken();

        mailNotificationService.send(userEntity.getEmail(), subject, body);
    }

    private void sendEmailApp(CustomerEntity userEntity) {
        //DESENVOLVER HTML PARA ENVIO DE EMAIL DE PROPAGANDA (PARA BAIXAR) O APP
//
//        String subject = "Validação de email - APP Pet System";
//        String body = "Seu token de validação é: " + userEntity.getEmailToken();
//
//        mailNotificationService.send(userEntity.getEmail(), subject, body);
    }

    public CustomerEntity changePassword(CustomerEntity authUser, CustomerChangePasswordRequest request) {
        CustomerEntity entity = repository.findByCpf(authUser.getCpf())
                .orElseThrow(GenericNotFoundException::new);

        if (!(BCrypt.checkpw(request.getOldPassword(), entity.getPassword())))
            throw new GenericIncorrectPasswordException();

        entity.setPassword(passwordEncoder.encode(request.getNewPassword()));
        entity.setChangePassword(false);
        entity = repository.save(entity);

        return entity;
    }

    private String generateEmailTokenValidate() {
        int number = (int) (1000 + Math.random() * (9999 - 1000 + 1));
        return String.valueOf(number);
    }

    public CustomerResponse emailValidate(Principal authentication, EmailValidateRequest request) {
        try {
            CustomerEntity userEntity = findByEmail(authentication.getName());
            if (request.emailToken().equalsIgnoreCase(userEntity.getEmailToken()) &&
                    !emailTokenExpired(userEntity.getEmailTokenTime())) {
                userEntity.setEmailValidated(true);

                save(userEntity);

                return convert.entityIntoResponse(userEntity);
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

    public CustomerResponse emailValidateResend(Principal authentication) {
        try {
            CustomerEntity userEntity = findByEmail(authentication.getName());

            sendValidationEmailToken(userEntity);

            return convert.entityIntoResponse(userEntity);

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

    private String generatePassword() {
        String newPassword = UUID.randomUUID().toString();
        return newPassword.substring(0,6);
    }


    public CustomerResponse update(Principal authentication, AppUserUpdateRequest request) {
        try {
            CustomerEntity userEntity = findByEmail(authentication.getName());
            CustomerEntity newEntity = convert.updateRequestIntoEntity(request, userEntity);
            if (request.getPassword() != null)
                newEntity.setPassword(passwordEncoder.encode(request.getPassword()));
            save(newEntity);
            return convert.entityIntoResponse(newEntity);
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

    public CustomerEntity findByEmail(String email) {
        return repository.findByEmailAndActiveIsTrue(email)
                .orElseThrow(GenericNotFoundException::new);
    }

    public CustomerResponse getByEmail(Principal authentication) {
        try {
            CustomerEntity userEntity = findByEmail(authentication.getName());
            if (!userEntity.getEmailValidated())
                sendValidationEmailToken(userEntity);

            CustomerResponse response = convert.entityIntoResponse(userEntity);

//            Set<AppAddressResponse> addressResponse = addressService.get(authentication);
//            response.setAddresses(addressResponse);
//
//            Set<PetResponse> petResponse = petService.get(authentication);
//            response.setPets(petResponse);

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

    public CustomerEntity save (CustomerEntity entity) {
        return repository.save(entity);
    }

    public void deactivate(String email) {
        try {
            CustomerEntity userEntity = findByEmail(email);
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
