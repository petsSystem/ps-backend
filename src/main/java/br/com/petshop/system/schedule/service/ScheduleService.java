package br.com.petshop.system.schedule.service;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericForbiddenException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.exception.GenericParamMissingException;
import br.com.petshop.system.company.model.entity.CompanyEntity;
import br.com.petshop.system.company.service.CompanyService;
import br.com.petshop.system.schedule.model.dto.request.ScheduleCreateRequest;
import br.com.petshop.system.schedule.model.dto.request.ScheduleFilterRequest;
import br.com.petshop.system.schedule.model.dto.request.ScheduleUpdateRequest;
import br.com.petshop.system.schedule.model.dto.response.ScheduleResponse;
import br.com.petshop.system.schedule.model.entity.ScheduleEntity;
import br.com.petshop.system.schedule.model.enums.Message;
import br.com.petshop.system.schedule.repository.ScheduleRepository;
import br.com.petshop.system.schedule.repository.ScheduleSpecification;
import br.com.petshop.system.user.model.entity.SysUserEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
public class ScheduleService {
    Logger log = LoggerFactory.getLogger(ScheduleService.class);
    @Autowired private ScheduleRepository employeeRepository;
    @Autowired private ScheduleSpecification specification;
    @Autowired private ScheduleConverterService convert;
    @Autowired private CompanyService companyService;
    @Autowired private ObjectMapper objectMapper;

    public ScheduleResponse create(Principal authentication, ScheduleCreateRequest request) {
        try {
//            Optional<ScheduleEntity> entity = employeeRepository.findByCpfAndActiveIsTrue(request.getCpf());
//
//            if (entity.isPresent())
//                throw new GenericAlreadyRegisteredException();
//
//            CompanyEntity companyEntity = companyService.findByIdAndActiveIsTrue(request.getCompanyId());
//
//            ScheduleEntity employeeEntity = convert.createRequestIntoEntity(request);
//            employeeEntity.setCompanyIds(List.of(request.getCompanyId()));
//            employeeRepository.save(employeeEntity);
//
//            return convert.entityIntoResponse(employeeEntity);

            return null;

        } catch (GenericAlreadyRegisteredException ex) {
            log.error(Message.EMPLOYEE_ALREADY_REGISTERED.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, Message.EMPLOYEE_ALREADY_REGISTERED.get(), ex);
        } catch (GenericNotFoundException ex) {
            log.error(Message.EMPLOYEE_ERROR_CREATE_COMPANY.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.EMPLOYEE_ERROR_CREATE_COMPANY.get(), ex);
        } catch (Exception ex) {
            log.error(Message.EMPLOYEE_ERROR_CREATE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.EMPLOYEE_ERROR_CREATE.get(), ex);
        }
    }

    public ScheduleResponse partialUpdate(UUID employeeId, JsonPatch patch) {
        try {
            ScheduleEntity entity = employeeRepository.findById(employeeId)
                    .orElseThrow(GenericNotFoundException::new);

            entity = applyPatch(patch, entity);
            entity = employeeRepository.save(entity);

            return  convert.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.EMPLOYEE_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.EMPLOYEE_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.EMPLOYEE_ERROR_PARTIAL.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.EMPLOYEE_ERROR_PARTIAL.get(), ex);
        }
    }

    private ScheduleEntity applyPatch(JsonPatch patch, ScheduleEntity entity) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        return objectMapper.treeToValue(patched, ScheduleEntity.class);
    }

    public  Page<ScheduleResponse> get(Principal authentication, Pageable pageable, ScheduleFilterRequest filter) {
        try {
            SysUserEntity systemUser = ((SysUserEntity) ((UsernamePasswordAuthenticationToken)
                    authentication).getPrincipal());

            if (systemUser.getRole() == Role.ADMIN)
                return get(pageable, filter);

            filter = validateCompanyIds(systemUser, filter);

            return get(pageable, filter);

        } catch (GenericParamMissingException ex) {
            log.error(Message.EMPLOYEE_ERROR_COMPANY_ID_SELECT.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, Message.EMPLOYEE_ERROR_COMPANY_ID_SELECT.get(), ex);
        } catch (GenericForbiddenException ex) {
            log.error(Message.EMPLOYEE_ERROR_COMPANY_ID_FORBIDDEN.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, Message.EMPLOYEE_ERROR_COMPANY_ID_FORBIDDEN.get(), ex);
        } catch (Exception ex) {
            log.error(Message.EMPLOYEE_ERROR_GET.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.EMPLOYEE_ERROR_GET.get(), ex);
        }
    }

    private  Page<ScheduleResponse> get(Pageable pageable, ScheduleFilterRequest filter) {
            Specification<ScheduleEntity> filters = specification.filter(filter);

            Page<ScheduleEntity> entities = null;//employeeRepository.findAll(filters, pageable);

            List<ScheduleResponse> response = entities.stream()
                    .map(c -> convert.entityIntoResponse(c))
                    .collect(Collectors.toList());

            return new PageImpl<>(response);
    }

    private ScheduleFilterRequest validateCompanyIds(SysUserEntity systemUser, ScheduleFilterRequest filter) {
        List<UUID> companyIds = systemUser.getEmployee().getCompanyIds();

        if (filter.getCompanyId() == null) {
            if (companyIds.size() > 1) //para casos de owner
                throw new GenericParamMissingException();

            //filter.setCompanyId(companyIds.get(0));
        } else { //checar se o employee pertence ao companyId
            if (!companyIds.contains(filter.getCompanyId()))
                throw new GenericForbiddenException();
        }
        return filter;
    }

    public ScheduleResponse getById(Principal authentication, UUID employeeId) {
        try {
            SysUserEntity systemUser = ((SysUserEntity) ((UsernamePasswordAuthenticationToken)
                    authentication).getPrincipal());

            if (systemUser.getRole() == Role.ADMIN) {
                ScheduleEntity entity = employeeRepository.findById(employeeId).orElseThrow(GenericNotFoundException::new);
                return convert.entityIntoResponse(entity);
            }

            ScheduleEntity entity = null;//employeeRepository.findByIdAndActiveIsTrue(employeeId)
//                    .orElseThrow(GenericNotFoundException::new);

            validateUserAccess(systemUser, entity);

            return convert.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.EMPLOYEE_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.EMPLOYEE_NOT_FOUND.get(), ex);
        } catch (GenericForbiddenException ex) {
            log.error(Message.EMPLOYEE_ERROR_FORBIDDEN.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, Message.EMPLOYEE_ERROR_FORBIDDEN.get(), ex);
        } catch (Exception ex) {
            log.error(Message.EMPLOYEE_ERROR_GET.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.EMPLOYEE_ERROR_GET.get(), ex);
        }
    }

    private void validateUserAccess(SysUserEntity systemUser, ScheduleEntity entity) {
//        if (systemUser.getRole() != Role.ADMIN) {
//            if (!systemUser.getEmployee().getCompanyIds().contains(entity.getCompanyIds().get(0)))
//                throw new GenericForbiddenException();
//        }
    }

    public ScheduleResponse updateById(Principal authentication, UUID employeeId, ScheduleUpdateRequest request) {
        try {
            ScheduleEntity entity = null;//employeeRepository.findByIdAndActiveIsTrue(employeeId)
//                    .orElseThrow(GenericNotFoundException::new);

            SysUserEntity systemUser = ((SysUserEntity) ((UsernamePasswordAuthenticationToken)
                    authentication).getPrincipal());

            if (systemUser.getRole() != Role.ADMIN) {
                List<UUID> companyIds = systemUser.getEmployee().getCompanyIds();
                Boolean contains = false;
                for (UUID companyId : companyIds) {
//                    if (entity.getCompanyIds().contains(companyId)) {
//                        contains = true;
//                        break;
//                    }
                }
                if (!contains)
                    throw new GenericForbiddenException();
            }

            entity = convert.updateRequestIntoEntity(request, entity);
            entity = employeeRepository.save(entity);

            return convert.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.EMPLOYEE_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.EMPLOYEE_NOT_FOUND.get(), ex);
        } catch (GenericForbiddenException ex) {
            log.error(Message.EMPLOYEE_ERROR_COMPANY_ID_FORBIDDEN.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, Message.EMPLOYEE_ERROR_COMPANY_ID_FORBIDDEN.get(), ex);
        } catch (Exception ex) {
            log.error(Message.EMPLOYEE_ERROR_UPDATE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.EMPLOYEE_ERROR_UPDATE.get(), ex);
        }
    }

    public void delete(UUID employeeId) {
        try {
            ScheduleEntity entity = employeeRepository.findById(employeeId)
                    .orElseThrow(GenericNotFoundException::new);
            employeeRepository.delete(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.EMPLOYEE_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.EMPLOYEE_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.EMPLOYEE_ERROR_DELETE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.EMPLOYEE_ERROR_DELETE.get(), ex);
        }
    }

    public ScheduleEntity findByIdAndActive(UUID employeeId) {
        Optional<ScheduleEntity> entity = null;//employeeRepository.findByIdAndActiveIsTrue(employeeId);
        if (entity.isEmpty())
            throw new GenericNotFoundException();
        return entity.get();
    }
}
