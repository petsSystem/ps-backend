package br.com.petshop.system.company.service;

import br.com.petshop.authentication.model.enums.Role;
import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericForbiddenException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.system.company.model.dto.request.CompanyCreateRequest;
import br.com.petshop.system.company.model.dto.request.CompanyUpdateRequest;
import br.com.petshop.system.company.model.dto.response.CompanyResponse;
import br.com.petshop.system.company.model.entity.CompanyEntity;
import br.com.petshop.system.company.model.enums.Message;
import br.com.petshop.system.schedule.service.ScheduleService;
import br.com.petshop.system.user.model.entity.SysUserEntity;
import com.github.fge.jsonpatch.JsonPatch;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CompanyValidateService {
    Logger log = LoggerFactory.getLogger(CompanyService.class);
    @Autowired CompanyService service;
    @Autowired private CompanyConverterService convert;
    @Autowired private ScheduleService scheduleService;

    public CompanyResponse create(Principal authentication, CompanyCreateRequest request) {
        try {

            CompanyEntity entityRequest = convert.createRequestIntoEntity(request);

            CompanyEntity entity = service.create(entityRequest);

            List<UUID> scheduleIds = scheduleService.create(entity);
            entity.setScheduleIds(scheduleIds);

            service.save(entity);

            return convert.entityIntoResponse(entity);

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

    public CompanyResponse activate (Principal authentication, UUID companyId, JsonPatch patch) {
        try {

            CompanyEntity entity = service.activate(companyId, patch);

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

            if (getRole(authentication) == Role.ADMIN)
                entities = service.findAll(paging);
            else
                entities = service.findByEmployeeId(getAuthUser(authentication).getEmployee().getId(), paging);

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

            if (getRole(authentication) != Role.ADMIN) {
                if (!getAuthUser(authentication).getEmployee().getCompanyIds().contains(companyId))
                    throw new GenericForbiddenException();
            }

            CompanyEntity entity = service.findByIdAndActiveIsTrue(companyId);
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

            CompanyEntity entityRequest = convert.updateRequestIntoEntity(request);

            CompanyEntity entity = service.updateById(companyId, entityRequest);

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

//    public void delete(Principal authentication, UUID companyId) {
//        try {
//
//            service.delete(companyId);
//
//        } catch (GenericNotFoundException ex) {
//            log.error(Message.COMPANY_NOT_FOUND.get() + " Error: " + ex.getMessage());
//            throw new ResponseStatusException(
//                    HttpStatus.NOT_FOUND, Message.COMPANY_NOT_FOUND.get(), ex);
//        } catch (Exception ex) {
//            log.error(Message.COMPANY_ERROR_DELETE.get() + " Error: " + ex.getMessage());
//            throw new ResponseStatusException(
//                    HttpStatus.BAD_REQUEST, Message.COMPANY_ERROR_DELETE.get(), ex);
//        }
//    }

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
