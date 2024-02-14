package br.com.petshop.product.service;

import br.com.petshop.category.model.dto.response.CategoryResponse;
import br.com.petshop.category.service.CategoryBusinessService;
import br.com.petshop.commons.exception.GenericAlreadyRegisteredException;
import br.com.petshop.commons.exception.GenericForbiddenException;
import br.com.petshop.commons.exception.GenericNotFoundException;
import br.com.petshop.commons.service.AuthenticationCommonService;
import br.com.petshop.product.model.dto.request.ProductCreateRequest;
import br.com.petshop.product.model.dto.request.ProductUpdateRequest;
import br.com.petshop.product.model.dto.response.ProductResponse;
import br.com.petshop.product.model.dto.response.ProductTableResponse;
import br.com.petshop.product.model.entity.ProductEntity;
import br.com.petshop.product.model.enums.Message;
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
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductBusinessService extends AuthenticationCommonService {
    private Logger log = LoggerFactory.getLogger(ProductService.class);
    @Autowired private ProductValidateService validate;
    @Autowired private ProductService service;
    @Autowired private CategoryBusinessService categoryBusinessService;
    @Autowired private ProductConverterService converter;

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

    public Page<ProductTableResponse> getAll(Principal authentication, Pageable paging, UUID companyId, Boolean additional) {
        try {
            //recupera todos os produtos pelo companyId
            Page<ProductEntity> entities = service.findAllByCompanyId(companyId, additional, paging);

            //recupera todas as categorias ativas pelo companyId
            List<CategoryResponse> categories = categoryBusinessService
                    .getAllByCompanyId(null, companyId, true);

            //separo cada categoria em um map, sendo o id da categoria a chave
            Map<UUID, CategoryResponse> mapCategories = categories.stream()
                    .collect(Collectors.toMap(CategoryResponse::getId, Function.identity()));

            //converto a entidade em resposta e seto o tipo de categoria na resposta
            List<ProductTableResponse> response = entities.stream()
                    .map(c -> {
                        ProductTableResponse resp = converter.entityIntoTableResponse(c);
                        resp.setCategory(mapCategories.get(c.getCategoryId()).getType());
                        return resp;
                    })
                    .collect(Collectors.toList());

            //ordena por status + nome
            Collections.sort(response, Comparator.comparing(ProductTableResponse::getActive).reversed()
                    .thenComparing(ProductTableResponse::getName));

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
}