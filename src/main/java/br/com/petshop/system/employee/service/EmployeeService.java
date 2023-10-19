package br.com.petshop.system.employee.service;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericParamMissingException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.exception.GenericForbiddenException;
import br.com.petshop.system.company.model.entity.CompanyEntity;
import br.com.petshop.system.company.service.CompanyService;
import br.com.petshop.system.employee.model.dto.request.EmployeeCreateRequest;
import br.com.petshop.system.employee.model.dto.request.EmployeeFilterRequest;
import br.com.petshop.system.employee.model.dto.request.EmployeeUpdateRequest;
import br.com.petshop.system.employee.model.dto.response.EmployeeResponse;
import br.com.petshop.system.employee.model.entity.EmployeeEntity;
import br.com.petshop.system.employee.model.enums.Message;
import br.com.petshop.system.employee.repository.EmployeeRepository;
import br.com.petshop.system.employee.repository.EmployeeSpecification;
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
public class EmployeeService {
    Logger log = LoggerFactory.getLogger(EmployeeService.class);
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private EmployeeSpecification specification;
    @Autowired private EmployeeConverterService convert;
    @Autowired private CompanyService companyService;
    @Autowired private ObjectMapper objectMapper;

    public EmployeeResponse create(Principal authentication, EmployeeCreateRequest request) {
        try {
            Optional<EmployeeEntity> entity = employeeRepository.findByCpfAndActiveIsTrue(request.getCpf());

            if (entity.isPresent())
                throw new GenericAlreadyRegisteredException();

            CompanyEntity companyEntity = companyService.findByIdAndActiveIsTrue(request.getCompanyId());

            EmployeeEntity employeeEntity = convert.createRequestIntoEntity(request);
            employeeEntity.setCompanyIds(List.of(request.getCompanyId()));
            employeeRepository.save(employeeEntity);

            return convert.entityIntoResponse(employeeEntity);

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

    public EmployeeResponse partialUpdate(UUID employeeId, JsonPatch patch) {
        try {
            EmployeeEntity entity = employeeRepository.findById(employeeId)
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

    private EmployeeEntity applyPatch(JsonPatch patch, EmployeeEntity entity) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        return objectMapper.treeToValue(patched, EmployeeEntity.class);
    }

    public  Page<EmployeeResponse> get(Principal authentication, Pageable pageable, EmployeeFilterRequest filter) {
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

    private  Page<EmployeeResponse> get(Pageable pageable, EmployeeFilterRequest filter) {
            Specification<EmployeeEntity> filters = specification.filter(filter);

            Page<EmployeeEntity> entities = employeeRepository.findAll(filters, pageable);

            List<EmployeeResponse> response = entities.stream()
                    .map(c -> convert.entityIntoResponse(c))
                    .collect(Collectors.toList());

            return new PageImpl<>(response);
    }

    private EmployeeFilterRequest validateCompanyIds(SysUserEntity systemUser, EmployeeFilterRequest filter) {
        List<UUID> companyIds = systemUser.getEmployee().getCompanyIds();

        if (filter.getCompanyId() == null) {
            if (companyIds.size() > 1) //para casos de owner
                throw new GenericParamMissingException();

            filter.setCompanyId(companyIds.get(0));
        } else { //checar se o employee pertence ao companyId
            if (filter.getCompanyId() != companyIds.get(0))
                throw new GenericForbiddenException();
        }
        return filter;
    }

    public EmployeeResponse updateById(Principal authentication, UUID employeeId, EmployeeUpdateRequest request) {
        try {
            EmployeeEntity entity = employeeRepository.findByIdAndActiveIsTrue(employeeId)
                    .orElseThrow(GenericNotFoundException::new);

            SysUserEntity systemUser = ((SysUserEntity) ((UsernamePasswordAuthenticationToken)
                    authentication).getPrincipal());

            if (systemUser.getRole() != Role.ADMIN) {
                List<UUID> companyIds = systemUser.getEmployee().getCompanyIds();
                for (UUID companyId : companyIds) {
                    if (entity.getCompanyIds().contains(companyId))
                        throw new GenericForbiddenException();
                }
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

    public EmployeeResponse update(Principal authentication, EmployeeUpdateRequest request) {
        try {
            EmployeeEntity entity = employeeRepository.findByIdAndActiveIsTrue(null)
                    .orElseThrow(GenericNotFoundException::new);

            entity = convert.updateRequestIntoEntity(request, entity);
            entity = employeeRepository.save(entity);

            return convert.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.EMPLOYEE_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error(Message.EMPLOYEE_ERROR_UPDATE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.EMPLOYEE_ERROR_UPDATE.get(), ex);
        }
    }

    public EmployeeResponse getById(Principal authentication, UUID employeeId) {
        try {
            EmployeeEntity entity = employeeRepository.findByIdAndActiveIsTrue(employeeId)
                    .orElseThrow(GenericNotFoundException::new);

            return convert.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.EMPLOYEE_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error(Message.EMPLOYEE_ERROR_GET.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.EMPLOYEE_ERROR_GET.get(), ex);
        }
    }



    public void deactivate(UUID employeeId) {
        try {
            EmployeeEntity entity = employeeRepository.findByIdAndActiveIsTrue(employeeId)
                    .orElseThrow(GenericNotFoundException::new);
            entity.setActive(false);
            employeeRepository.save(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.EMPLOYEE_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error(Message.EMPLOYEE_ERROR_DEACTIVATE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.EMPLOYEE_ERROR_DEACTIVATE.get(), ex);
        }
    }

    public void activate(UUID employeeId) {
        try {
            EmployeeEntity entity = employeeRepository.findById(employeeId)
                    .orElseThrow(GenericNotFoundException::new);
            entity.setActive(true);
            employeeRepository.save(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.EMPLOYEE_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error(Message.EMPLOYEE_ERROR_ACTIVATE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.EMPLOYEE_ERROR_ACTIVATE.get(), ex);
        }
    }

    public void delete(UUID employeeId) {
        try {
            EmployeeEntity entity = employeeRepository.findById(employeeId)
                    .orElseThrow(GenericNotFoundException::new);
            employeeRepository.delete(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.EMPLOYEE_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error(Message.EMPLOYEE_ERROR_DELETE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.EMPLOYEE_ERROR_DELETE.get(), ex);
        }
    }
}
