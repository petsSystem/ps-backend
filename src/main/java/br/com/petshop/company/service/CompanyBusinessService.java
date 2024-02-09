package br.com.petshop.company.service;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.commons.service.AuthenticationCommonService;
import br.com.petshop.category.service.CategoryService;
import br.com.petshop.company.model.dto.request.CompanyCreateRequest;
import br.com.petshop.company.model.dto.request.CompanyUpdateRequest;
import br.com.petshop.company.model.dto.response.CompanyResponse;
import br.com.petshop.company.model.dto.response.CompanySummaryResponse;
import br.com.petshop.company.model.entity.CompanyEntity;
import br.com.petshop.company.model.enums.Message;
import br.com.petshop.customer.model.entity.CustomerEntity;
import br.com.petshop.customer.service.GeometryService;
import br.com.petshop.commons.exception.GenericAlreadyRegisteredException;
import br.com.petshop.commons.exception.GenericForbiddenException;
import br.com.petshop.commons.exception.GenericNotActiveException;
import br.com.petshop.commons.exception.GenericNotFoundException;
import com.github.fge.jsonpatch.JsonPatch;
import org.locationtech.jts.geom.Point;
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

@Service
public class CompanyBusinessService extends AuthenticationCommonService {
    private Logger log = LoggerFactory.getLogger(CompanyService.class);
    @Autowired private CompanyService service;
    @Autowired private CompanyConverterService convert;
    @Autowired private CategoryService categoryService;
    @Autowired private GeometryService geometry;

    public CompanyResponse create(Principal authentication, CompanyCreateRequest request) {
        try {

            CompanyEntity entityRequest = convert.createRequestIntoEntity(request);

            CompanyEntity entity = service.create(entityRequest);

            entity = service.save(entity);

            //create all categories
            categoryService.createAutomatic(entity.getId());

            return convert.entityIntoResponse(entity);

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

    public CompanyResponse activate (Principal authentication, UUID companyId, JsonPatch patch) {
        try {

            CompanyEntity entity = service.activate(companyId, patch);

            return  convert.entityIntoResponse(entity);

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

    public CompanyResponse updateById(Principal authentication, UUID companyId, CompanyUpdateRequest request) {
        try {

            CompanyEntity entityRequest = convert.updateRequestIntoEntity(request);

            CompanyEntity entity = service.updateById(companyId, entityRequest);

            return convert.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.COMPANY_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.COMPANY_NOT_FOUND_ERROR.get(), ex);
        } catch (GenericNotActiveException ex) {
            log.error(Message.COMPANY_NOT_ACTIVE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.COMPANY_NOT_ACTIVE_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.COMPANY_UPDATE_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.COMPANY_UPDATE_ERROR.get(), ex);
        }
    }

    public Page<CompanyResponse> get(Principal authentication, Pageable paging) {
        try {
            Page<CompanyEntity> entities;

            if (getSysRole(authentication) == Role.ADMIN)
                entities = service.findAll(paging);
            else
                entities = service.findByEmployeeId(getSysAuthUser(authentication).getId(), paging);

            List<CompanyResponse> response = entities.stream()
                    .map(c -> convert.entityIntoResponse(c))
                    .collect(Collectors.toList());

            Collections.sort(response, Comparator.comparing(CompanyResponse::getActive).reversed()
                    .thenComparing(CompanyResponse::getName));

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

    public CompanyResponse getById(Principal authentication, UUID companyId) {
        try {

            if (getSysRole(authentication) != Role.ADMIN) {
                if (!getSysAuthUser(authentication).getCompanyIds().contains(companyId))
                    throw new GenericForbiddenException();
            }

            CompanyEntity entity = service.findById(companyId);
            return convert.entityIntoResponse(entity);

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

    public List<CompanySummaryResponse> nearby(Principal authentication, Double lat, Double lon, Double radius) {
        try {
            Point point = geometry.getPoint(lat, lon);

            List<CompanyEntity> companies = service.nearby(point, radius);

            //adicionar as lojas favoritas no inicio da lista dos mais proximos
            CustomerEntity customer = getAppAuthUser(authentication);
            customer.getFavorites().stream()
                    .forEach(c -> {
                        CompanyEntity companyEntity = service.findById(c);
                        companies.add(0, companyEntity);
                    });

            return companies.stream()
                    .map(c -> {
                        CompanySummaryResponse response = convert.entityIntoAppResponse(c);
                        Double dist = service.getDistance(point, c.getId());
                        response.setDistance(dist);
                        return response;
                    })
                    .collect(Collectors.toList());

        } catch (GenericNotFoundException ex) {
            log.error(Message.COMPANY_NOT_FOUND_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.COMPANY_NOT_FOUND_ERROR.get(), ex);
        } catch (Exception ex) {
            log.error(Message.COMPANY_GEO_ERROR.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.COMPANY_GEO_ERROR.get(), ex);
        }
    }

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
                        return convert.entityIntoResponse(entity);
                }
            }
            return convert.entityIntoResponse(entity);

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
