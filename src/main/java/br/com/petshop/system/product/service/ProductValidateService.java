package br.com.petshop.system.product.service;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.system.category.model.dto.response.CategoryResponse;
import br.com.petshop.system.product.model.dto.request.ProductCreateRequest;
import br.com.petshop.system.product.model.dto.request.ProductUpdateRequest;
import br.com.petshop.system.product.model.dto.response.ProductResponse;
import br.com.petshop.system.product.model.dto.response.ProductTableResponse;
import br.com.petshop.system.product.model.entity.ProductEntity;
import br.com.petshop.system.product.model.enums.Message;
import br.com.petshop.system.schedule.service.ScheduleService;
import br.com.petshop.system.user.model.entity.SysUserEntity;
import com.github.fge.jsonpatch.JsonPatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductValidateService {
    Logger log = LoggerFactory.getLogger(ProductService.class);
    @Autowired
    ProductService service;
    @Autowired private ProductConverterService convert;
    @Autowired private ScheduleService scheduleService;

    public ProductResponse create(Principal authentication, ProductCreateRequest request) {
        try {

            ProductEntity entityRequest = convert.createRequestIntoEntity(request);

            ProductEntity entity = service.create(entityRequest);

            service.save(entity);

            return convert.entityIntoResponse(entity);

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

            return  convert.entityIntoResponse(entity);

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

            ProductEntity entityRequest = convert.updateRequestIntoEntity(request);

            ProductEntity entity = service.updateById(productId, entityRequest);

            return convert.entityIntoResponse(entity);

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

    public List<ProductTableResponse> getByCategoryId(Principal authentication, UUID categoryId) {
        try {
            List<ProductEntity> entities = service.findAllByCategoryId(categoryId);

            List<ProductTableResponse> response = entities.stream()
                    .map(c -> convert.entityIntoTableResponse(c))
                    .collect(Collectors.toList());

            Collections.sort(response, Comparator.comparing(ProductTableResponse::getActive).reversed()
                    .thenComparing(ProductTableResponse::getName));

            return response;

        } catch (Exception ex) {
            log.error(Message.PRODUCT_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.PRODUCT_GET_ERROR.get(), ex);
        }
    }

    public ProductResponse getById(Principal authentication, UUID productId) {
        try {
            ProductEntity entity = service.findById(productId);
            return convert.entityIntoResponse(entity);

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

    private SysUserEntity getAuthUser(Principal authentication) {
        return ((SysUserEntity) ((UsernamePasswordAuthenticationToken)
                authentication).getPrincipal());
    }

    private Role getRole(Principal authentication) {
        SysUserEntity systemUser = ((SysUserEntity) ((UsernamePasswordAuthenticationToken)
                authentication).getPrincipal());

        return systemUser.getRole();
    }
}
