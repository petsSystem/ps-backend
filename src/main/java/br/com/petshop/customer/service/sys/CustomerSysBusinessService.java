package br.com.petshop.customer.service.sys;

import br.com.petshop.commons.exception.GenericAlreadyRegisteredException;
import br.com.petshop.commons.exception.GenericNotFoundException;
import br.com.petshop.commons.service.AuthenticationCommonService;
import br.com.petshop.customer.model.dto.request.sys.CustomerSysCreateRequest;
import br.com.petshop.customer.model.dto.request.sys.CustomerSysUpdateRequest;
import br.com.petshop.customer.model.dto.response.CustomerResponse;
import br.com.petshop.customer.model.dto.response.CustomerTableResponse;
import br.com.petshop.customer.model.entity.CustomerEntity;
import br.com.petshop.customer.model.enums.Message;
import br.com.petshop.notification.MailNotificationService;
import br.com.petshop.notification.MailType;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Classe responsável pelas regras de negócio do cliente do sistema web.
 */
@Service
public class CustomerSysBusinessService extends AuthenticationCommonService {

    private Logger log = LoggerFactory.getLogger(CustomerSysBusinessService.class);
    @Autowired private CustomerSysService service;
    @Autowired private MailNotificationService mailService;
    @Autowired private CustomerSysConverterService converter;

    /**
     * Método de criação de cliente do sistema web.
     * @param request - dto com dados de criação de cliente
     * @return - dados do cliente
     */
    public CustomerResponse create(CustomerSysCreateRequest request) {
        try {
            //converte request em entidade
            UserDetails user = converter.createRequestIntoEntity(request);
            CustomerEntity entity = (CustomerEntity) user;
            entity.setCompanyIds(List.of(request.getCompanyId()));
            entity.setFavorites(List.of(request.getCompanyId()));

            //cria a entidade customer
            entity = service.create(entity);

            //envio email com link do APP
            mailService.sendEmail(entity.getName(), entity.getEmail(), "", MailType.APP_INVITATION);

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

    /**
     * Método que atualiza dados do cliente no sistema web.
     * @param authentication - dados do usuário logado
     * @param customerId - id do cadastro do cliente
     * @param request - dto com dados de atualização do cliente
     * @return - dados do cliente
     */
    public CustomerResponse update(Principal authentication, UUID customerId, CustomerSysUpdateRequest request) {
        try {
            //recupera cliente pelo id
            CustomerEntity entity = service.findById(customerId);

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

    /**
     * Método que associa um cliente a loja/petshop no sistema web.
     * @param authentication - dados do usuário logado
     * @param customerId - id de cadastro da loja/petshop
     * @param patch - dados de associação do cliente a loja/petshop
     * @return - dados do cliente
     */
    public CustomerResponse associateCompanyId(Principal authentication, UUID customerId, JsonPatch patch) {
        try {
            //recupera o cliente pelo id
            CustomerEntity entity = service.findById(customerId);

            //associo cliente
            entity = service.associateCompanyId(entity, patch);

            //converte a entidade na resposta final
            return converter.entityIntoResponse(entity);

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

    /**
     * Método que retorna dados de clientes de uma determinada loja/petshop.
     * @param authentication - dados do usuário logado
     * @param companyId - id de cadastro da loja/petshop
     * @param pageable - dados de paginação
     * @return - lista dos dados do cliente
     */
    public Page<CustomerTableResponse> get(Principal authentication, UUID companyId, Pageable pageable) {
        try {
            //recupera todos os clientes por companyID
            Page<CustomerEntity> entities = service.findAllByCompanyId(companyId, pageable);

            //converte entidade para a resposta
            List<CustomerTableResponse> response = entities.stream()
                    .map(c -> {
                        CustomerTableResponse resp = converter.entityIntoTableResponse(c);
                        resp.setFavorite(c.getFavorites().contains(companyId));

                        return resp;
                    })
                    .collect(Collectors.toList());

            //ordena por status + nome
            Collections.sort(response, Comparator.comparing(CustomerTableResponse::getActive).reversed()
                    .thenComparing(CustomerTableResponse::getName));

            //pagina a lista
            return new PageImpl<>(response);

        } catch (Exception ex) {
            log.error(Message.CUSTOMER_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.CUSTOMER_GET_ERROR.get(), ex);
        }
    }

    /**
     * Método que retorna um cliente do sistema web, através da informação do seu id.
     * @param authentication - dados do usuário logado
     * @param id - id do cadastro do cliente
     * @return - dados do cliente
     */
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
