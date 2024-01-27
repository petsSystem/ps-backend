package br.com.petshop.customer.service;

import br.com.petshop.authentication.service.AuthenticationCommonService;
import br.com.petshop.customer.model.dto.request.CustomerAppCreateRequest;
import br.com.petshop.customer.model.dto.request.CustomerChangePasswordRequest;
import br.com.petshop.customer.model.dto.request.CustomerSysUpdateRequest;
import br.com.petshop.customer.model.dto.request.EmailValidateRequest;
import br.com.petshop.customer.model.dto.response.CustomerResponse;
import br.com.petshop.customer.model.entity.CustomerEntity;
import br.com.petshop.customer.model.enums.Message;
import br.com.petshop.exception.EmailTokenException;
import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericIncorrectPasswordException;
import br.com.petshop.exception.GenericNotFoundException;
import com.github.fge.jsonpatch.JsonPatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Service
public class CustomerAppFacade extends AuthenticationCommonService {

    private Logger log = LoggerFactory.getLogger(CustomerAppFacade.class);
    @Autowired private CustomerService service;
    @Autowired private CustomerConverterService converter;

    public void forget(String email) {
        try {
            service.forget(email);

        } catch (GenericNotFoundException ex) {
            log.error(Message.CUSTOMER_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.CUSTOMER_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.CUSTOMER_SEND_EMAIL_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.CUSTOMER_SEND_EMAIL_ERROR.get(), ex);
        }
    }

    public CustomerResponse create(CustomerAppCreateRequest request) {
        try {
            UserDetails user = converter.appCreateRequestIntoEntity(request);

            CustomerEntity entity = service.create((CustomerEntity) user);

            return converter.entityIntoResponse(entity);

        } catch (GenericAlreadyRegisteredException ex) {
            log.error(Message.CUSTOMER_ALREADY_REGISTERED_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, Message.CUSTOMER_ALREADY_REGISTERED_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.CUSTOMER_CREATE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.CUSTOMER_CREATE_ERROR.get(), ex);
        }
    }

    public CustomerResponse update(Principal authentication, CustomerSysUpdateRequest request) {
        try {
            CustomerEntity authUser = getAppAuthUser(authentication);
            CustomerEntity entity = service.update(authUser.getId(), request);

            return converter.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.CUSTOMER_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.CUSTOMER_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.CUSTOMER_UPDATE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.CUSTOMER_UPDATE_ERROR.get(), ex);
        }
    }

    public CustomerResponse me(Principal authentication) {
        try {
            CustomerEntity userEntity = service.findById(getAppAuthUser(authentication).getId());

            return converter.entityIntoResponse(userEntity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.CUSTOMER_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.CUSTOMER_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.CUSTOMER_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.CUSTOMER_GET_ERROR.get(), ex);
        }
    }

    public CustomerResponse changePassword(Principal authentication, CustomerChangePasswordRequest request) {
        try {
            CustomerEntity authUser = getAppAuthUser(authentication);
            CustomerEntity entity = service.changePassword(authUser, request);

            return converter.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.CUSTOMER_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.CUSTOMER_NOT_FOUND_ERROR.get(), ex);
        } catch (GenericIncorrectPasswordException ex) {
            log.error(Message.CUSTOMER_OLD_PASSWORD_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.CUSTOMER_OLD_PASSWORD_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.CUSTOMER_CHANGING_PASSWORD_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.CUSTOMER_CHANGING_PASSWORD_ERROR.get(), ex);
        }
    }

    public CustomerResponse emailValidate(Principal authentication, EmailValidateRequest request) {
        try {
            CustomerEntity entity = service.emailValidate(
                    getAppAuthUser(authentication).getCpf(), request.emailToken());

            return converter.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.CUSTOMER_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.CUSTOMER_NOT_FOUND_ERROR.get(), ex);
        } catch (EmailTokenException ex) {
            log.error(Message.CUSTOMER_UNAUTHORIZED_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED, Message.CUSTOMER_UNAUTHORIZED_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.CUSTOMER_VALIDATE_EMAIL_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.CUSTOMER_VALIDATE_EMAIL_ERROR.get(), ex);
        }
    }

    public void resendEmailValidate(Principal authentication) {
        try {
            service.resendEmailValidate(getAppAuthUser(authentication).getCpf());

        } catch (GenericNotFoundException ex) {
            log.error(Message.CUSTOMER_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.CUSTOMER_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.CUSTOMER_RESEND_TOKEN_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.CUSTOMER_RESEND_TOKEN_ERROR.get(), ex);
        }
    }

    public void deactivate(Principal authentication, JsonPatch patch) {
        try {
            service.deactivate(getAppAuthUser(authentication).getId(), patch);

        } catch (GenericNotFoundException ex) {
            log.error(Message.CUSTOMER_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.CUSTOMER_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.CUSTOMER_DELETE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.CUSTOMER_DELETE_ERROR.get(), ex);
        }
    }
}
