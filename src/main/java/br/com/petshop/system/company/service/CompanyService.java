package br.com.petshop.system.company.service;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericForbiddenException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.system.company.model.dto.request.CompanyCreateRequest;
import br.com.petshop.system.company.model.dto.request.CompanyUpdateRequest;
import br.com.petshop.system.company.model.dto.response.CompanyResponse;
import br.com.petshop.system.company.model.dto.response.CompanySummaryResponse;
import br.com.petshop.system.company.model.entity.CompanyEntity;
import br.com.petshop.system.company.model.enums.Message;
import br.com.petshop.system.company.repository.CompanyRepository;
import br.com.petshop.system.user.model.entity.SysUserEntity;
import br.com.petshop.utils.PetGeometry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CompanyService {
    Logger log = LoggerFactory.getLogger(CompanyService.class);
    @Autowired private CompanyRepository companyRepository;
    @Autowired private CompanyConverterService convert;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private PetGeometry geometry;

    public CompanyResponse create(Principal authentication, CompanyCreateRequest request) {
        try {
            Optional<CompanyEntity> company = companyRepository.findByCnpj(request.getCnpj());
            if (company.isPresent())
                throw new GenericAlreadyRegisteredException();

            CompanyEntity companyEntity = convert.createRequestIntoEntity(request);
            companyEntity.setGeom((Point)
                    geometry.getPoint(companyEntity.getAddress().getLat(), companyEntity.getAddress().getLon()));

            companyEntity = companyRepository.save(companyEntity);

            return convert.entityIntoResponse(companyEntity);

        } catch (GenericAlreadyRegisteredException ex) {
            log.error( Message.COMPANY_ALREADY_REGISTERED.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, Message.COMPANY_ALREADY_REGISTERED.get(), ex);
        } catch (Exception ex) {
            log.error(Message.COMPANY_ERROR_CREATE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.COMPANY_ERROR_CREATE.get(), ex);
        }
    }

    public CompanyResponse partialUpdate(UUID companyId, JsonPatch patch) {
        try {
            CompanyEntity entity = companyRepository.findById(companyId)
                    .orElseThrow(GenericNotFoundException::new);

            entity = applyPatch(patch, entity);
            entity = companyRepository.save(entity);

            return  convert.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.COMPANY_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.COMPANY_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.COMPANY_ERROR_PARTIAL.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.COMPANY_ERROR_PARTIAL.get(), ex);
        }
    }

    public Page<CompanyResponse> get(Principal authentication, Pageable paging) {
        try {
            Page<CompanyEntity> entities;
            SysUserEntity systemUser = ((SysUserEntity) ((UsernamePasswordAuthenticationToken)
                    authentication).getPrincipal());

            if (systemUser.getRole() == Role.ADMIN)
                entities = companyRepository.findAll(paging);
            else
                entities = findByEmployeeId(systemUser.getEmployee().getId(), paging);

            List<CompanyResponse> response = entities.stream()
                    .map(c -> convert.entityIntoResponse(c))
                    .collect(Collectors.toList());

            return new PageImpl<>(response);

        } catch (Exception ex) {
            log.error(Message.COMPANY_ERROR_GET.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.COMPANY_ERROR_GET.get(), ex);
        }
    }

    public CompanyResponse getById(Principal authentication, UUID companyId) {
        try {
            validateUserAccess(authentication, companyId);

            CompanyEntity entity = findByIdAndActiveIsTrue(companyId);
            return convert.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.COMPANY_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.COMPANY_NOT_FOUND.get(), ex);
        } catch (GenericForbiddenException ex) {
            log.error(Message.COMPANY_ERROR_FORBIDDEN.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, Message.COMPANY_ERROR_FORBIDDEN.get(), ex);
        } catch (Exception ex) {
            log.error(Message.COMPANY_ERROR_GET.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.COMPANY_ERROR_GET.get(), ex);
        }
    }

    public CompanyResponse updateById(Principal authentication, UUID companyId, CompanyUpdateRequest request) {
        try {
            validateUserAccess(authentication, companyId);
            CompanyEntity entity = findByIdAndActiveIsTrue(companyId);

            entity = convert.updateRequestIntoEntity(request, entity);
            entity = companyRepository.save(entity);

            return convert.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.COMPANY_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.COMPANY_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.COMPANY_ERROR_UPDATE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.COMPANY_ERROR_UPDATE.get(), ex);
        }
    }

    private void validateUserAccess(Principal authentication, UUID companyId) {
        SysUserEntity systemUser = ((SysUserEntity) ((UsernamePasswordAuthenticationToken)
                authentication).getPrincipal());

        if (systemUser.getRole() != Role.ADMIN) {
            if (!systemUser.getEmployee().getCompanyIds().contains(companyId))
                throw new GenericForbiddenException();
        }
    }

    public void delete(UUID companyId) {
        try {
            CompanyEntity entity = companyRepository.findById(companyId)
                    .orElseThrow(GenericNotFoundException::new);
            companyRepository.delete(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.COMPANY_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.COMPANY_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.COMPANY_ERROR_DELETE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.COMPANY_ERROR_DELETE.get(), ex);
        }
    }

    private CompanyEntity applyPatch(JsonPatch patch, CompanyEntity entity) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        return objectMapper.treeToValue(patched, CompanyEntity.class);
    }

    public CompanyEntity findByIdAndActiveIsTrue(UUID companyId) {
        return companyRepository.findByIdAndActiveIsTrue(companyId)
                .orElseThrow(GenericNotFoundException::new);
    }

    public Page<CompanyEntity> findByEmployeeId(UUID employeeId, Pageable paging) {
        return companyRepository.findCompaniesFromEmployeeId(employeeId, paging);
    }

    public List<CompanySummaryResponse> findAround(Point p, Double distance) {
        List<CompanyEntity> companies = companyRepository.findNearWithinDistance(p, distance);
        if (companies.isEmpty())
            throw new GenericNotFoundException();
        return companies.stream()
                .map(c -> {
                    CompanySummaryResponse response = convert.entityIntoAppResponse(c);
                    Double dist = companyRepository.getDistance(p, c.getId());
                    response.setDistance(dist);
                    return response;
                })
                .collect(Collectors.toList());
    }
}
