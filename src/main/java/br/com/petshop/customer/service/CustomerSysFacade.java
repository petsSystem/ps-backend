package br.com.petshop.customer.service;

import br.com.petshop.authentication.service.AuthenticationCommonService;
import br.com.petshop.customer.model.dto.request.CustomerSysCreateRequest;
import br.com.petshop.customer.model.dto.request.CustomerSysUpdateRequest;
import br.com.petshop.customer.model.dto.response.CustomerResponse;
import br.com.petshop.customer.model.dto.response.CustomerTableResponse;
import br.com.petshop.customer.model.entity.CustomerEntity;
import br.com.petshop.customer.model.enums.Message;
import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericNotFoundException;
import com.github.fge.jsonpatch.JsonPatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerSysFacade extends AuthenticationCommonService {

    private Logger log = LoggerFactory.getLogger(CustomerSysFacade.class);
    @Autowired private CustomerService service;
    @Autowired private CustomerConverterService converter;

    public CustomerResponse create(CustomerSysCreateRequest request) {
        try {
            UserDetails user = converter.sysCreateRequestIntoEntity(request);
            CustomerEntity entity = (CustomerEntity) user;

            List<UUID> companyIds = new ArrayList<>();
            companyIds.add(request.getCompanyId());
            entity.setCompanyIds(companyIds);
            entity.setFavorites(companyIds);

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

    public CustomerResponse associateCompanyId(Principal authentication, UUID customerId, JsonPatch patch) {
        try {
            CustomerEntity entity = service.associateCompanyId(customerId, patch, false);

            return  converter.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.CUSTOMER_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.CUSTOMER_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.CUSTOMER_ASSOCIATE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.CUSTOMER_ASSOCIATE_ERROR.get(), ex);
        }
    }

    public CustomerResponse update(Principal authentication, UUID customerId, CustomerSysUpdateRequest request) {
        try {
            CustomerEntity entity = service.update(customerId, request);

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

    public Page<CustomerTableResponse> get(Principal authentication, UUID companyId, Pageable pageable) {
        try {
            Page<CustomerEntity> entities = service.findAllByCompanyId(companyId, pageable);

            List<CustomerTableResponse> response = entities.stream()
                    .map(c -> {
                        CustomerTableResponse resp = converter.entityIntoTableResponse(c);
                        resp.setFavorite(c.getFavorites().contains(companyId));

                        return resp;
                    })
                    .collect(Collectors.toList());

            Collections.sort(response, Comparator.comparing(CustomerTableResponse::getActive).reversed()
                    .thenComparing(CustomerTableResponse::getName));

            return new PageImpl<>(response);

        } catch (Exception ex) {
            log.error(Message.CUSTOMER_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.CUSTOMER_GET_ERROR.get(), ex);
        }
    }

    public CustomerResponse getById(Principal authentication, UUID id) {
        try {
            CustomerEntity entity = service.findById(id);

            return converter.entityIntoResponse(entity);

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
}
