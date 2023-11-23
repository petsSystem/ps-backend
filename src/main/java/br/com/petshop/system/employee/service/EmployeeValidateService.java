package br.com.petshop.system.employee.service;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericForbiddenException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.exception.GenericParamMissingException;
import br.com.petshop.system.employee.model.dto.request.EmployeeCreateRequest;
import br.com.petshop.system.employee.model.dto.request.EmployeeFilterRequest;
import br.com.petshop.system.employee.model.dto.request.EmployeeUpdateRequest;
import br.com.petshop.system.employee.model.dto.response.EmployeeResponse;
import br.com.petshop.system.employee.model.entity.EmployeeEntity;
import br.com.petshop.system.employee.model.enums.Message;
import br.com.petshop.system.user.model.entity.SysUserEntity;
import com.github.fge.jsonpatch.JsonPatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
public class EmployeeValidateService {
    Logger log = LoggerFactory.getLogger(EmployeeValidateService.class);
    @Autowired private EmployeeService service;
    @Autowired private EmployeeConverterService convert;

    public EmployeeResponse create(Principal authentication, EmployeeCreateRequest request) {
        try {

            EmployeeEntity employeeEntity = convert.createRequestIntoEntity(request);
            employeeEntity = service.create(employeeEntity, request.getCompanyId());

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

    public EmployeeResponse partialUpdate(Principal authentication, UUID employeeId, JsonPatch patch) {
        try {

            EmployeeEntity entity = service.partialUpdate(employeeId, patch);

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

    public  Page<EmployeeResponse> get(Principal authentication, Pageable pageable, EmployeeFilterRequest filter) {
        try {

            if (getRole(authentication) == Role.ADMIN)
                return service.get(pageable, filter);

            filter = validateCompanyIds(getAuthUser(authentication), filter);

            return service.get(pageable, filter);

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

    private EmployeeFilterRequest validateCompanyIds(SysUserEntity systemUser, EmployeeFilterRequest filter) {
        List<UUID> companyIds = systemUser.getEmployee().getCompanyIds();

        if (filter.getCompanyId() == null) {
            if (companyIds.size() > 1) //para casos de owner
                throw new GenericParamMissingException();

            filter.setCompanyId(companyIds.get(0));
        } else { //checar se o employee pertence ao companyId
            if (!companyIds.contains(filter.getCompanyId()))
                throw new GenericForbiddenException();
        }
        return filter;
    }

    public EmployeeResponse getById(Principal authentication, UUID employeeId) {
        try {

            if (getRole(authentication) == Role.ADMIN) {
                EmployeeEntity entity = service.findById(employeeId);
                return convert.entityIntoResponse(entity);
            }

            EmployeeEntity entity = service.findByIdAndActiveIsTrue(employeeId);

            validateUserAccess(getAuthUser(authentication), entity);

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

    private void validateUserAccess(SysUserEntity systemUser, EmployeeEntity entity) {
        if (systemUser.getRole() != Role.ADMIN) {
            if (!systemUser.getEmployee().getCompanyIds().contains(entity.getCompanyIds().get(0)))
                throw new GenericForbiddenException();
        }
    }

    public EmployeeResponse updateById(Principal authentication, UUID employeeId, EmployeeUpdateRequest request) {
        try {
            EmployeeEntity entity = service.findByIdAndActiveIsTrue(employeeId);

            if (getRole(authentication) != Role.ADMIN) {
                List<UUID> companyIds = getAuthUser(authentication).getEmployee().getCompanyIds();
                Boolean contains = false;
                for (UUID companyId : companyIds) {
                    if (entity.getCompanyIds().contains(companyId)) {
                        contains = true;
                        break;
                    }
                }
                if (!contains)
                    throw new GenericForbiddenException();
            }

            EmployeeEntity entityRequest =  convert.updateRequestIntoEntity(request);

            entity = service.updateById(entityRequest, entity);

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

    public void delete(Principal authentication, UUID employeeId) {
        try {

            service.delete(employeeId);

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
