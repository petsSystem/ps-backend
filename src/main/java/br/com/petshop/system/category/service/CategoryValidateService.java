package br.com.petshop.system.category.service;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.system.category.model.dto.request.CategoryCreateRequest;
import br.com.petshop.system.category.model.dto.request.CategoryUpdateRequest;
import br.com.petshop.system.category.model.dto.response.CategoryResponse;
import br.com.petshop.system.category.model.dto.response.CategoryTableResponse;
import br.com.petshop.system.category.model.entity.CategoryEntity;
import br.com.petshop.system.category.model.enums.Message;
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
public class CategoryValidateService {
    Logger log = LoggerFactory.getLogger(CategoryService.class);
    @Autowired
    CategoryService service;
    @Autowired private CategoryConverterService convert;
    @Autowired private ScheduleService scheduleService;

    public CategoryResponse create(Principal authentication, CategoryCreateRequest request) {
        try {

            CategoryEntity entityRequest = convert.createRequestIntoEntity(request);

            CategoryEntity entity = service.create(entityRequest);

            service.save(entity);

            return convert.entityIntoResponse(entity);

        } catch (GenericAlreadyRegisteredException ex) {
            log.error( Message.CATEGORY_ALREADY_REGISTERED_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, Message.CATEGORY_ALREADY_REGISTERED_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.CATEGORY_CREATE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.CATEGORY_CREATE_ERROR.get(), ex);
        }
    }

    public CategoryResponse activate (Principal authentication, UUID categoryId, JsonPatch patch) {
        try {

            CategoryEntity entity = service.activate(categoryId, patch);

            return  convert.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.CATEGORY_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.CATEGORY_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.CATEGORY_ACTIVATE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.CATEGORY_ACTIVATE_ERROR.get(), ex);
        }
    }

    public CategoryResponse updateById(Principal authentication, UUID categoryId, CategoryUpdateRequest request) {
        try {

            CategoryEntity entityRequest = convert.updateRequestIntoEntity(request);

            CategoryEntity entity = service.updateById(categoryId, entityRequest);

            return convert.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.CATEGORY_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.CATEGORY_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.CATEGORY_UPDATE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.CATEGORY_UPDATE_ERROR.get(), ex);
        }
    }

    public List<CategoryTableResponse> getByCompanyId(Principal authentication, UUID companyId) {
        try {
            List<CategoryEntity> entities = service.findAllByCompanyId(companyId);

            List<CategoryTableResponse> response = entities.stream()
                    .map(c -> convert.entityIntoTableResponse(c))
                    .collect(Collectors.toList());

            Collections.sort(response, Comparator.comparing(CategoryTableResponse::getActive).reversed()
                    .thenComparing(CategoryTableResponse::getLabel));

            return response;

        } catch (Exception ex) {
            log.error(Message.CATEGORY_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.CATEGORY_GET_ERROR.get(), ex);
        }
    }

    public CategoryResponse getById(Principal authentication, UUID categoryId) {
        try {
            CategoryEntity entity = service.findById(categoryId);
            return convert.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.CATEGORY_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.CATEGORY_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.CATEGORY_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.CATEGORY_GET_ERROR.get(), ex);
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
