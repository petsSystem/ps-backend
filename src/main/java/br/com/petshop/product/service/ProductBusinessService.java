package br.com.petshop.product.service;

import br.com.petshop.category.model.dto.response.CategoryResponse;
import br.com.petshop.category.service.CategoryBusinessService;
import br.com.petshop.commons.exception.GenericAlreadyRegisteredException;
import br.com.petshop.commons.exception.GenericForbiddenException;
import br.com.petshop.commons.exception.GenericNotFoundException;
import br.com.petshop.commons.service.AuthenticationCommonService;
import br.com.petshop.product.model.dto.request.ProductCreateRequest;
import br.com.petshop.product.model.dto.request.ProductUpdateRequest;
import br.com.petshop.product.model.dto.response.AdditionalResponse;
import br.com.petshop.product.model.dto.response.ProductResponse;
import br.com.petshop.product.model.dto.response.ProductTableResponse;
import br.com.petshop.product.model.entity.ProductEntity;
import br.com.petshop.product.model.enums.Message;
import br.com.petshop.schedule.model.dto.request.ScheduleFilterRequest;
import br.com.petshop.schedule.model.dto.response.ScheduleResponse;
import br.com.petshop.schedule.service.ScheduleBusinessService;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Classe responsável pelas regras de negócio do produto/serviço
 */
@Service
public class ProductBusinessService extends AuthenticationCommonService {
    private Logger log = LoggerFactory.getLogger(ProductService.class);
    @Autowired private ProductValidateService validate;
    @Autowired private ProductService service;
    @Autowired private CategoryBusinessService categoryBusinessService;
    @Autowired private ProductConverterService converter;
    @Autowired private ScheduleBusinessService scheduleBusinessService;

    /**
     * Método de criação de produto/serviço.
     * @param authentication - dados do usuário logado
     * @param request - dto contendo dados de criação do produto/serviço
     * @return - dados do produto/serviço
     */
    public ProductResponse create(Principal authentication, ProductCreateRequest request) {
        try {
            //valida acesso a loja
            validate.accessByCompany(authentication, request.getCompanyId());

            //converte request em entidade
            ProductEntity entityRequest = converter.createRequestIntoEntity(request);

            //cria a entidade product
            ProductEntity entity = service.create(entityRequest);

            //converte a entidade na resposta final
            return converter.entityIntoResponse(entity);

        } catch (GenericForbiddenException ex) {
            log.error( Message.PRODUCT_FORBIDDEN_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, Message.PRODUCT_FORBIDDEN_ERROR.get(), ex);
        } catch (GenericAlreadyRegisteredException ex) {
            log.error( Message.PRODUCT_ALREADY_REGISTERED_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, Message.PRODUCT_ALREADY_REGISTERED_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.PRODUCT_CREATE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PRODUCT_CREATE_ERROR.get(), ex);
        }
    }

    /**
     * Método de atualização de produto/serviço, através de id informado.
     * @param authentication - dados do usuário logado
     * @param productId - id do cadastro do produto/serviço
     * @param request - dto contendo dados de atualização do produto/serviço
     * @return - dados do produto/serviço
     */
    public ProductResponse updateById(Principal authentication, UUID productId, ProductUpdateRequest request) {
        try {
            //recupera o produto ativo pelo id
            ProductEntity entity = service.findByIdAndActiveIsTrue(productId);

            //valida acesso a loja
            validate.accessByCompany(authentication, entity.getCompanyId());

            //converte request em entidade
            ProductEntity entityRequest = converter.updateRequestIntoEntity(request);
            entity = converter.updateRequestIntoEntity(entityRequest, entity);

            //faz atualizaçao da entidade
            entity = service.updateById(entity);

            //converte a entidade na resposta final
            return converter.entityIntoResponse(entity);

        } catch (GenericForbiddenException ex) {
            log.error( Message.PRODUCT_FORBIDDEN_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, Message.PRODUCT_FORBIDDEN_ERROR.get(), ex);
        } catch (GenericNotFoundException ex) {
            log.error(Message.PRODUCT_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.PRODUCT_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.PRODUCT_UPDATE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PRODUCT_UPDATE_ERROR.get(), ex);
        }
    }

    /**
     * Método de ativação/desativação de produto/serviço.
     * @param authentication - dados do usuário logado
     * @param productId - id do cadastro do produto/serviço
     * @param patch - dados de ativação/desativação do produto/serviço
     * @return - dados do produto/serviço
     */
    public ProductResponse activate (Principal authentication, UUID productId, JsonPatch patch) {
        try {
            //recupera o produto pelo id
            ProductEntity entity = service.findById(productId);

            //valida acesso a loja
            validate.accessByCompany(authentication, entity.getCompanyId());

            //ativa/desativa produto
            entity = service.activate(entity, patch);

            //converte a entidade na resposta final
            return converter.entityIntoResponse(entity);

        } catch (GenericForbiddenException ex) {
            log.error( Message.PRODUCT_FORBIDDEN_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, Message.PRODUCT_FORBIDDEN_ERROR.get(), ex);
        } catch (GenericNotFoundException ex) {
            log.error(Message.PRODUCT_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.PRODUCT_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.PRODUCT_ACTIVATE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PRODUCT_ACTIVATE_ERROR.get(), ex);
        }
    }

    /**
     * Método que recupera dados de produto/serviço, através de filtro informado.
     * @param authentication - dados do usuário logado
     * @param paging - dados de paginação
     * @param companyId - id do cadastro da loja/petshop
     * @param categoryId - id do cadastro da categoria
     * @param additional - filtro de pesquisa.
     *                   Se true = buscará todos os produtos/serviços adicionais
     *                   Se false = buscará todos os produtos/serviços principais
     *                   Se null (não informado) = retornará todos os produtos/serviços
     * @return - dados do produto/serviço
     */
    public Page<ProductTableResponse> getAll(Principal authentication, Pageable paging, UUID companyId, UUID categoryId, Boolean additional) {
        try {
            //recupera todos os produtos pelo companyId
            Page<ProductEntity> entities = service.findAllByCompanyId(companyId, categoryId, additional, paging);

            List<ProductTableResponse> response = getAllCommons(authentication, entities, companyId, categoryId);

            return new PageImpl<>(response);

        } catch (GenericForbiddenException ex) {
            log.error( Message.PRODUCT_FORBIDDEN_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, Message.PRODUCT_FORBIDDEN_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.PRODUCT_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PRODUCT_GET_ERROR.get(), ex);
        }
    }

    /**
     * Método que recupera dados de produto/serviço, através de filtro informado.
     * @param authentication - dados do usuário logado
     * @param companyId - id do cadastro da loja/petshop
     * @param categoryId - id do cadastro da categoria
     * @param additional - filtro de pesquisa.
     *                   Se true = buscará todos os produtos/serviços adicionais
     *                   Se false = buscará todos os produtos/serviços principais
     *                   Se null (não informado) = retornará todos os produtos/serviços
     * @return - dados do produto/serviço
     */
    public List<ProductTableResponse> getAll(Principal authentication, UUID companyId, UUID categoryId, Boolean additional) {
        try {
            //recupera todos os produtos pelo filtro
            List<ProductEntity> entities = service.findAll(companyId, categoryId, additional);

            return getAllCommons(authentication, new PageImpl<>(entities), companyId, categoryId);

        } catch (GenericForbiddenException ex) {
            log.error( Message.PRODUCT_FORBIDDEN_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, Message.PRODUCT_FORBIDDEN_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.PRODUCT_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PRODUCT_GET_ERROR.get(), ex);
        }
    }

    private List<ProductTableResponse> getAllCommons(Principal authentication, Page<ProductEntity> entities, UUID companyId, UUID categoryId) {
        //checo se há agenda para todos os produtos
        List<CategoryResponse> categories = null;

        if (categoryId == null) {
            //recupera todas as categorias pelo companyId
            categories = categoryBusinessService
                    .getAllByCompanyId(null, companyId, true);
        } else { //fluxo é de agendamento
            //recupera a categoria pelo categoryId
            categories = List.of(categoryBusinessService
                    .getById(authentication, categoryId));

            //checo se há agendas para o produto.
            entities = checkSchedule(authentication, companyId, categoryId, entities);
        }

        //separo cada categoria em um map, sendo o id da categoria a chave
        Map<UUID, CategoryResponse> mapCategories = categories.stream()
                .collect(Collectors.toMap(CategoryResponse::getId, Function.identity()));

        //converto a entidade em resposta e seto o tipo de categoria na resposta
        List<ProductTableResponse> response = entities.stream()
                .map(c -> {
                    ProductTableResponse resp = converter.entityIntoTableResponse(c);
                    resp.setCategory(mapCategories.get(c.getCategoryId()).getType());
                    resp.setAdditionals(getAdditionals(resp.getAdditionalIds()));
                    return resp;
                })
                .collect(Collectors.toList());

        //ordena por status + nome
        Collections.sort(response, Comparator.comparing(ProductTableResponse::getActive).reversed()
                .thenComparing(ProductTableResponse::getName));

        return response;
    }

    /**
     * Método que recupera lista de adicioais de determinado produto.
     * @param additionalIds - id do cadastro do produto/serviço como adicional de outro produto/serviço
     * @return - lista dos dados do produto/serviço
     */
    private List<AdditionalResponse> getAdditionals(List<UUID> additionalIds) {
        return additionalIds.stream()
                .map(a -> converter.entityIntoAdditionalResponse(service.findById(a)))
                .collect(Collectors.toList());
    }

    /**
     * Método que verifica se há agenda para um determinado produto/serviço.
     * Esse método será utilizado para o fluxo de agendamneto.
     * @param authentication - dados do usuário logado
     * @param companyId - id do cadastro da loja/petshop
     * @param categoryId - id do cadastro da categoria
     * @param entities - lista dos dados do produto/serviço
     * @return - lista dos dados do produto/serviço
     */
    private Page<ProductEntity> checkSchedule(Principal authentication, UUID companyId,
                                              UUID categoryId, Page<ProductEntity> entities) {
        List<ProductEntity> entitiesFiltered = new ArrayList<>();

        ScheduleFilterRequest filter = ScheduleFilterRequest.builder()
                .companyId(companyId)
                .categoryId(categoryId)
                .build();

        entities.forEach(e -> {
            filter.setProductId(e.getId());
            List<ScheduleResponse> schedules = scheduleBusinessService
                    .getByFilter(authentication, filter);

            if (!schedules.isEmpty())
                entitiesFiltered.add(e);
        });

        return new PageImpl<>(entitiesFiltered);
    }

    /**
     * Método que retorna dados de um produto/serviço, através da informação do id.
     * @param authentication - dados do usuário logado
     * @param productId - id do cadastro do produto/serviço
     * @return - dados do produto/serviço
     */
    public ProductResponse getById(Principal authentication, UUID productId) {
        try {
            //recupera o produto
            ProductEntity entity = service.findById(productId);

            //valida acesso a loja
            validate.accessByCompany(authentication, entity.getCompanyId());

            //converte entidade para a resposta
            return converter.entityIntoResponse(entity);

        } catch (GenericForbiddenException ex) {
            log.error( Message.PRODUCT_FORBIDDEN_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, Message.PRODUCT_FORBIDDEN_ERROR.get(), ex);
        } catch (GenericNotFoundException ex) {
            log.error(Message.PRODUCT_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.PRODUCT_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.PRODUCT_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PRODUCT_GET_ERROR.get(), ex);
        }
    }

    /**
     * Método que retorna dados de um produto/serviço, através da informação do id.
     * @param productId - id do cadastro do produto/serviço
     * @return - dados do produto/serviço
     */
    public ProductTableResponse findById(Principal authentication, UUID productId) {
        try {
            //recupera o produto
            ProductEntity entity = service.findById(productId);

            //valida acesso a loja
            validate.accessByCompany(authentication, entity.getCompanyId());

            CategoryResponse category = categoryBusinessService
                    .getById(authentication, entity.getCategoryId());

            //converte entidade para a resposta
            ProductTableResponse response = converter.entityIntoTableResponse(entity);
            response.setCategory(category.getType());

            return response;

        } catch (GenericForbiddenException ex) {
            log.error( Message.PRODUCT_FORBIDDEN_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, Message.PRODUCT_FORBIDDEN_ERROR.get(), ex);
        } catch (GenericNotFoundException ex) {
            log.error(Message.PRODUCT_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.PRODUCT_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.PRODUCT_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PRODUCT_GET_ERROR.get(), ex);
        }
    }
}