package br.com.petshop.company.service;

import br.com.petshop.category.service.CategoryBusinessService;
import br.com.petshop.commons.exception.GenericAlreadyRegisteredException;
import br.com.petshop.commons.exception.GenericForbiddenException;
import br.com.petshop.commons.exception.GenericNotFoundException;
import br.com.petshop.commons.service.AuthenticationCommonService;
import br.com.petshop.company.model.dto.request.CompanyCreateRequest;
import br.com.petshop.company.model.dto.request.CompanyUpdateRequest;
import br.com.petshop.company.model.dto.response.CompanyResponse;
import br.com.petshop.company.model.entity.CompanyEntity;
import br.com.petshop.company.model.enums.Message;
import com.github.fge.jsonpatch.JsonPatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Classe responsável pelas regras de negócio da loja/petshop
 */
@Service
public class CompanyBusinessService extends AuthenticationCommonService {
    private Logger log = LoggerFactory.getLogger(CompanyService.class);
    @Autowired private CompanyConverterService converter;
    @Autowired private CompanyService service;
    @Autowired private CompanyValidationService validate;
    @Autowired private CategoryBusinessService categoryBusinessService;

    /**
     * Método de criação de loja/petshop.
     * @param authentication - dados do usuário logado
     * @param request - dados de criação da loja/petshop
     * @return - dados da loja/petshop
     */
    public CompanyResponse create(Principal authentication, CompanyCreateRequest request) {
        try {
            //converte request em entidade
            CompanyEntity entityRequest = converter.createRequestIntoEntity(request);

            //cria a entidade company
            CompanyEntity entity = service.create(entityRequest);

            //criar todas as categorias inativas para a nova loja
            categoryBusinessService.createAutomatic(entity.getId());

            //converte a entidade na resposta final
            return converter.entityIntoResponse(entity);

        } catch (GenericAlreadyRegisteredException ex) {
            log.error( Message.COMPANY_ALREADY_REGISTERED_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, Message.COMPANY_ALREADY_REGISTERED_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.COMPANY_CREATE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.COMPANY_CREATE_ERROR.get(), ex);
        }
    }

    /**
     * Método de atualização de loja/petshop pelo id.
     * @param authentication - dados do usuário logado
     * @param companyId - id do cadastro da loja/petshop
     * @param request - dados de atualização da loja/petshop
     * @return - dados da loja/petshop
     */
    public CompanyResponse updateById(Principal authentication, UUID companyId, CompanyUpdateRequest request) {
        try {
            //recupera a loja ativa pelo id
            CompanyEntity entity = service.findByIdAndActiveIsTrue(companyId);

            //valida acesso a loja
            validate.accessByCompany(authentication, companyId);

            //converte request em entidade
            CompanyEntity entityRequest = converter.updateRequestIntoEntity(request);
            entity = converter.updateRequestIntoEntity(entityRequest, entity);

            //faz atualizaçao da entidade
            entity = service.updateById(entity);

            //converte a entidade na resposta final
            return converter.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.COMPANY_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.COMPANY_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.COMPANY_UPDATE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.COMPANY_UPDATE_ERROR.get(), ex);
        }
    }

    /**
     * Método de ativação/desativação de loja/petshop.
     * @param authentication - dados do usuário logado
     * @param companyId - id do cadastro da loja/petshop
     * @param patch - dados de ativação/desativação da loja/petshop
     * @return - dados da loja/petshop
     */
    public CompanyResponse activate (Principal authentication, UUID companyId, JsonPatch patch) {
        try {
            //recupera o loja pelo id
            CompanyEntity entity = service.findById(companyId);

            //ativa/desativa loja
            entity = service.activate(entity, patch);

            //converte a entidade na resposta final
            return converter.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.COMPANY_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.COMPANY_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.COMPANY_ACTIVATE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.COMPANY_ACTIVATE_ERROR.get(), ex);
        }
    }

    /**
     * Método que recupera todas as loja/petshop.
     * @param authentication - dados do usuário logado
     * @param paging - dados de paginação
     * @return - lista dos dados da loja/petshop
     */
    public Page<CompanyResponse> get(Principal authentication, Pageable paging) {
        try {
            //recupera todas as companies pelo tipo de acesso
            Page<CompanyEntity> entities;
            switch (getSysRole(authentication)) {
                case ADMIN -> entities = service.findAll(paging);
                default -> entities = service.findByCompanyIds(getSysAuthUser(authentication).getCompanyIds(), paging);
            }

            //converte entidade para a resposta
            List<CompanyResponse> response = entities.stream()
                    .map(c -> converter.entityIntoResponse(c))
                    .collect(Collectors.toList());

            //ordena por status + nome
            Collections.sort(response, Comparator.comparing(CompanyResponse::getActive).reversed()
                    .thenComparing(CompanyResponse::getName));

            //pagina a lista
            return new PageImpl<>(response);

        } catch (GenericNotFoundException ex) {
            log.error(Message.COMPANY_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.COMPANY_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.COMPANY_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.COMPANY_GET_ERROR.get(), ex);
        }
    }

    /**
     * Método que recupera todas as loja/petshop pelo id.
     * @param authentication - dados do usuário logado
     * @param companyId - id do cadastro da loja/petshop
     * @return - dados da loja/petshop
     */
    public CompanyResponse getById(Principal authentication, UUID companyId) {
        try {
            //valida acesso a loja
            validate.accessByCompany(authentication, companyId);

            //busca company pelo id
            CompanyEntity entity = service.findById(companyId);

            //converte entidade para a resposta
            return converter.entityIntoResponse(entity);

        } catch (GenericForbiddenException ex) {
            log.error(Message.COMPANY_FORBIDDEN_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, Message.COMPANY_FORBIDDEN_ERROR.get(), ex);
        } catch (GenericNotFoundException ex) {
            log.error(Message.COMPANY_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.COMPANY_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.COMPANY_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.COMPANY_GET_ERROR.get(), ex);
        }
    }

    /**
     * Método que recupera a loja/petshop ativa através do currentCompanyId. Caso este parâmetro não
     * seja informado, retornará a primeira loja/peshop ativo da lista de companyIds informado.
     * @param currentCompanyId - id da última loja/petshop acessado pelo usuário
     * @param companyIds - lista de ids das lojas/petshops associados ao usuário
     * @return - dados da loja/petshop
     */
    public CompanyResponse findActiveCompany(UUID currentCompanyId, List<UUID> companyIds) {
        try {
            CompanyEntity entity = null;
            if (currentCompanyId != null)
                entity = service.findById(currentCompanyId);
            else {
                //retorna a primeira companyId ativa da lista
                for(UUID companyId : companyIds) {
                    entity = service.findById(companyId);
                    if (entity.getActive())
                        return converter.entityIntoResponse(entity);
                }
            }
            return converter.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.COMPANY_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.COMPANY_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.COMPANY_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.COMPANY_GET_ERROR.get(), ex);
        }
    }
}
