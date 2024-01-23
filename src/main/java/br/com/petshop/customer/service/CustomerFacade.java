package br.com.petshop.customer.service;

import br.com.petshop.customer.model.dto.request.CustomerAppCreateRequest;
import br.com.petshop.customer.model.dto.request.CustomerChangePasswordRequest;
import br.com.petshop.customer.model.dto.request.CustomerSysCreateRequest;
import br.com.petshop.customer.model.dto.response.CustomerResponse;
import br.com.petshop.customer.model.entity.CustomerEntity;
import br.com.petshop.customer.model.enums.Message;
import br.com.petshop.authentication.service.AuthenticationCommonService;
import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericIncorrectPasswordException;
import br.com.petshop.exception.GenericNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CustomerFacade extends AuthenticationCommonService {

    private Logger log = LoggerFactory.getLogger(CustomerFacade.class);
    @Autowired private CustomerService service;
    @Autowired private CustomerConverterService converter;

    public void forget(String email) {
        try {
            service.forget(email);

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

    public CustomerResponse createApp(CustomerAppCreateRequest request) {
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

    public CustomerResponse createSys(CustomerSysCreateRequest request) {
        try {
            UserDetails user = converter.sysCreateRequestIntoEntity(request);
            CustomerEntity entity = (CustomerEntity) user;

            List<UUID> companyIds = new ArrayList<>();
            companyIds.add(request.getCompanyId());
            entity.setCompanyIds(companyIds);

            entity = service.create(entity);

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
}
