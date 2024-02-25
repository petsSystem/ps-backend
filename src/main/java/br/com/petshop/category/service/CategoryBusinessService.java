package br.com.petshop.category.service;

import br.com.petshop.category.model.dto.request.CategoryUpdateRequest;
import br.com.petshop.category.model.dto.response.CategoryResponse;
import br.com.petshop.category.model.entity.CategoryEntity;
import br.com.petshop.category.model.enums.Category;
import br.com.petshop.category.model.enums.Message;
import br.com.petshop.commons.exception.GenericForbiddenException;
import br.com.petshop.commons.exception.GenericNotFoundException;
import br.com.petshop.commons.service.AuthenticationCommonService;
import com.github.fge.jsonpatch.JsonPatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Classe responsável pelas regras de negócio da categoria
 */
@Service
public class CategoryBusinessService extends AuthenticationCommonService {
    private Logger log = LoggerFactory.getLogger(CategoryService.class);
    @Autowired private CategoryService service;
    @Autowired private CategoryValidateService validate;
    @Autowired private CategoryConverterService converter;

    /**
     * Método de criação automática de categorias.
     * É chamado na criação de uma loja/petshop.
     * @param companyId - id do cadastro da loja/petshop
     */
    public void createAutomatic(UUID companyId) {
        try {
            //criar todas as categorias inativas para uma nova loja
            Category.stream()
                    .forEach(c -> {
                        service.save(CategoryEntity.builder()
                                .companyId(companyId)
                                .type(c)
                                .description(c.getDescription())
                                .active(false)
                                .build());
                    });

        } catch (Exception ex) {
            log.error(Message.CATEGORY_CREATE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.CATEGORY_CREATE_ERROR.get(), ex);
        }
    }

    /**
     * Método de atualização de categoria pelo id.
     * @param authentication - dados do usuário logado
     * @param categoryId - id do cadastro da categoria
     * @param request - dados da categoria
     * @return
     */
    public CategoryResponse updateById(Principal authentication, UUID categoryId, CategoryUpdateRequest request) {
        try {
            //recupera a categoria ativa pelo id
            CategoryEntity entity = service.findByIdAndActiveIsTrue(categoryId);

            //valida acesso a loja
            validate.accessByCompany(authentication, entity.getCompanyId());

            //converte request em entidade
            CategoryEntity entityRequest = converter.updateRequestIntoEntity(request);
            entity = converter.updateRequestIntoEntity(entityRequest, entity);

            //faz atualizaçao da entidade
            entity = service.updateById(entity);

            //converte a entidade na resposta final
            return converter.entityIntoResponse(entity);

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

    /**
     * Método de ativação/destivação de categoria
     * @param authentication - dados do usuário logado
     * @param categoryId - id do cadastro da categoria
     * @param patch - Método de ativação/destivação de categoria
     * @return - dados da categoria
     */
    public CategoryResponse activate (Principal authentication, UUID categoryId, JsonPatch patch) {
        try {
            //recupera a categoria pelo id
            CategoryEntity entity = service.findById(categoryId);

            //ativa/desativa categoria
            entity = service.activate(entity, patch);

            //converte a entidade na resposta final
            return converter.entityIntoResponse(entity);

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

    /**
     * Método que retorna todas as categorias através dos filtros: company e active
     * @param authentication - dados do usuário logado
     * @param companyId - id do cadastro da loja/petshop
     * @param active - atributo que indica se a categoria deverá ser ativa ou inativa
     * @return - lista de dados da categoria
     */
    public List<CategoryResponse> getAllByCompanyId(Principal authentication, UUID companyId, Boolean active) {
        try {
            //recupera todas as categorias pelo companyId
            List<CategoryEntity> entities = service.findAllByCompanyId(companyId, active);

            //converte entidade para a resposta
            List<CategoryResponse> response = entities.stream()
                    .map(c -> converter.entityIntoResponse(c))
                    .collect(Collectors.toList());

            //ordena por status
            Collections.sort(response, Comparator.comparing(CategoryResponse::getActive).reversed());

            return response;

        } catch (Exception ex) {
            log.error(Message.CATEGORY_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.CATEGORY_GET_ERROR.get(), ex);
        }
    }

    /**
     * Método que recupera a categoria pelo id
     * @param authentication - dados do usuário logado
     * @param categoryId - id do cadastro da categoria
     * @return - dados da categoria
     */
    public CategoryResponse getById(Principal authentication, UUID categoryId) {
        try {
            //recupera a categoria
            CategoryEntity entity = service.findById(categoryId);

            //valida acesso a loja
            validate.accessByCompany(authentication, entity.getCompanyId());

            //converte entidade para a resposta
            return converter.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.CATEGORY_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.CATEGORY_NOT_FOUND_ERROR.get(), ex);
        } catch (GenericForbiddenException ex) {
            log.error(Message.CATEGORY_FORBIDDEN_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, Message.CATEGORY_FORBIDDEN_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.CATEGORY_GET_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.CATEGORY_GET_ERROR.get(), ex);
        }
    }
}
