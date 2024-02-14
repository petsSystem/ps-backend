package br.com.petshop.customer.service.app;

import br.com.petshop.commons.exception.EmailTokenException;
import br.com.petshop.commons.exception.GenericAlreadyRegisteredException;
import br.com.petshop.commons.exception.GenericIncorrectPasswordException;
import br.com.petshop.commons.exception.GenericNotFoundException;
import br.com.petshop.commons.service.AuthenticationCommonService;
import br.com.petshop.customer.model.dto.request.app.CustomerAppCreateRequest;
import br.com.petshop.customer.model.dto.request.app.CustomerAppUpdateRequest;
import br.com.petshop.customer.model.dto.request.app.CustomerChangePasswordRequest;
import br.com.petshop.customer.model.dto.request.app.EmailValidateRequest;
import br.com.petshop.customer.model.dto.response.CustomerResponse;
import br.com.petshop.customer.model.entity.CustomerEntity;
import br.com.petshop.customer.model.enums.Message;
import br.com.petshop.notification.MailNotificationService;
import br.com.petshop.notification.MailType;
import com.github.fge.jsonpatch.JsonPatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.UUID;

@Service
public class CustomerAppBusinessService extends AuthenticationCommonService {

    private Logger log = LoggerFactory.getLogger(CustomerAppBusinessService.class);
    @Autowired private CustomerAppService service;
    @Autowired private MailNotificationService mailService;
    @Autowired private CustomerAppConverterService converter;

    public CustomerResponse create(CustomerAppCreateRequest request) {
        try {
            //converte request em entidade
            UserDetails user = converter.createRequestIntoEntity(request);
            CustomerEntity entity = (CustomerEntity) user;

            //cria a entidade customer
            entity = service.create(entity);

            //envio email com token de validação
            mailService.sendEmail(entity.getName(), entity.getEmail(), entity.getEmailToken(), MailType.VALIDATE_EMAIL);

            //converte a entidade na resposta final
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
    public void forget(String cpf) {
        try {
            CustomerEntity entity = service.findByCpfAndActiveIsTrue(cpf);
            entity = service.forget(entity);

            //envio email com nova senha
            mailService.sendEmail(entity.getName(), entity.getEmail(), entity.getPassword(), MailType.NEW_PASSWORD);

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

    public CustomerResponse update(Principal authentication, CustomerAppUpdateRequest request) {
        try {
            //recupera cliente pelo id
            CustomerEntity authUser = getAppAuthUser(authentication);
            CustomerEntity entity = service.findById(authUser.getId());

            //converte request em entidade
            CustomerEntity entityRequest = converter.updateRequestIntoEntity(request);
            entity = converter.updateRequestIntoEntity(entityRequest, entity);

            //faz atualizaçao da entidade
            entity = service.updateById(entity);

            //converte a entidade na resposta final
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

    public CustomerResponse changePassword(Principal authentication, CustomerChangePasswordRequest request) {
        try {
            //recupera cliente pelo id
            CustomerEntity authUser = getAppAuthUser(authentication);
            CustomerEntity entity = service.findByCpfAndActiveIsTrue(authUser.getCpf());

            //faz a troca da senha
            entity = service.changePassword(entity, request);

            //converte a entidade na resposta final
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

    public CustomerResponse me(Principal authentication) {
        try {
            //recupera cliente pelo id
            CustomerEntity authUser = getAppAuthUser(authentication);
            CustomerEntity userEntity = service.findById(authUser.getId());

            //converte a entidade na resposta final
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

    public CustomerResponse favorite(Principal authentication, JsonPatch patch) {
        try {
            //recupera cliente pelo id
            UUID customerId = getAppAuthUser(authentication).getId();
            CustomerEntity entity = service.findById(customerId);

            //faz associação
            entity = service.associateCompanyId(entity, patch);

            //converte a entidade na resposta final
            return  converter.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.CUSTOMER_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.CUSTOMER_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.CUSTOMER_FAVORITE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.CUSTOMER_FAVORITE_ERROR.get(), ex);
        }
    }

    public CustomerResponse unfavorite(Principal authentication, JsonPatch patch) {
        try {
            //recupera cliente pelo id
            UUID customerId = getAppAuthUser(authentication).getId();
            CustomerEntity entity = service.findById(customerId);

            //faz desassociação
            entity = service.desassociateCompanyId(entity, patch);

            //converte a entidade na resposta final
            return  converter.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.CUSTOMER_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.CUSTOMER_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.CUSTOMER_UNFAVORITE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.CUSTOMER_UNFAVORITE_ERROR.get(), ex);
        }
    }

    public CustomerResponse emailValidate(Principal authentication, EmailValidateRequest request) {
        try {
            //recupera cliente pelo cpf
            CustomerEntity authUser = getAppAuthUser(authentication);
            CustomerEntity entity = service.findByCpfAndActiveIsTrue(authUser.getCpf());

            //valida token/email
            entity = service.emailValidate(entity, request.emailToken());

            //converte a entidade na resposta final
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
            //recupera cliente pelo cpf
            CustomerEntity authUser = getAppAuthUser(authentication);
            CustomerEntity entity = service.findByCpfAndActiveIsTrue(authUser.getCpf());

            //envio email com token de validação
            mailService.sendEmail(entity.getName(), entity.getEmail(), entity.getEmailToken(), MailType.VALIDATE_EMAIL);

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
            //recupera cliente pelo cpf
            CustomerEntity authUser = getAppAuthUser(authentication);
            CustomerEntity entity = service.findByCpfAndActiveIsTrue(authUser.getCpf());

            //desativo cliente
            service.deactivate(entity);

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
