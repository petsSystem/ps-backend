package br.com.petshop.product.service;

import br.com.petshop.commons.service.AuthenticationCommonService;
import br.com.petshop.category.model.entity.CategoryEntity;
import br.com.petshop.category.service.CategoryService;
import br.com.petshop.commons.exception.GenericAlreadyRegisteredException;
import br.com.petshop.commons.exception.GenericNotFoundException;
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
public class ProductFacadeService extends AuthenticationCommonService {
    private Logger log = LoggerFactory.getLogger(ProductService.class);
    @Autowired private ProductService service;
    @Autowired private CategoryService categoryService;
    @Autowired private ProductConverterService converter;

    public ProductResponse create(Principal authentication, ProductCreateRequest request) {
        try {

            ProductEntity entityRequest = converter.createRequestIntoEntity(request);

            ProductEntity entity = service.create(entityRequest);

            return converter.entityIntoResponse(entity);

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

    public ProductResponse activate (Principal authentication, UUID productId, JsonPatch patch) {
        try {

            ProductEntity entity = service.activate(productId, patch);

            return  converter.entityIntoResponse(entity);

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

    public ProductResponse updateById(Principal authentication, UUID productId, ProductUpdateRequest request) {
        try {

            ProductEntity entityRequest = converter.updateRequestIntoEntity(request);

            ProductEntity entity = service.updateById(productId, entityRequest);

            return converter.entityIntoResponse(entity);

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

    public Page<ProductTableResponse> getByCompanyId(Principal authentication, Pageable paging, UUID companyId) {
        try {
            Page<ProductEntity> entities = service.findAllByCompanyId(companyId, paging);

            List<CategoryEntity> categories = categoryService.findAllByCompanyId(companyId, true);
            Map<UUID, CategoryEntity> mapCategories = categories.stream()
                    .collect(Collectors.toMap(CategoryEntity::getId, Function.identity()));

            List<ProductTableResponse> response = entities.stream()
                    .map(c -> {
                        ProductTableResponse resp = converter.entityIntoTableResponse(c);
                        resp.setCategory(mapCategories.get(c.getCategoryId()).getType());
                        return resp;
                    })
                    .collect(Collectors.toList());

            Collections.sort(response, Comparator.comparing(ProductTableResponse::getActive).reversed()
                    .thenComparing(ProductTableResponse::getName));

            return new PageImpl<>(response);

        } catch (Exception ex) {
            log.error(Message.PRODUCT_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PRODUCT_GET_ERROR.get(), ex);
        }
    }

    public ProductResponse getById(Principal authentication, UUID productId) {
        try {
            ProductEntity entity = service.findById(productId);
            return converter.entityIntoResponse(entity);

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
