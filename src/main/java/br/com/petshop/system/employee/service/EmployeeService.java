package br.com.petshop.system.employee.service;

import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.system.employee.model.dto.request.EmployeeCreateRequest;
import br.com.petshop.system.employee.model.dto.request.EmployeeUpdateRequest;
import br.com.petshop.system.employee.model.dto.response.EmployeeResponse;
import br.com.petshop.system.employee.model.entity.EmployeeEntity;
import br.com.petshop.system.employee.model.enums.Message;
import br.com.petshop.system.employee.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Optional;

@Service
public class EmployeeService {
    Logger log = LoggerFactory.getLogger(EmployeeService.class);
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private EmployeeConverterService convert;

    public EmployeeResponse create(Principal authentication, EmployeeCreateRequest request) {
        try {
            Optional<EmployeeEntity> employee = employeeRepository.findByCpfAndActiveIsTrue(request.getCpf());
            if (employee.isPresent())
                throw new GenericAlreadyRegisteredException(Message.EMPLOYEE_ALREADY_REGISTERED.get());

            EmployeeEntity employeeEntity = convert.createRequestIntoEntity(request);

            employeeEntity = employeeRepository.save(employeeEntity);

            return convert.entityIntoResponse(employeeEntity);

        } catch (GenericAlreadyRegisteredException ex) {
            log.error("Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error(Message.EMPLOYEE_ERROR_CREATE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.EMPLOYEE_ERROR_CREATE.get(), ex);
        }
    }

    public EmployeeResponse updateById(Principal authentication, String employeeId, EmployeeUpdateRequest request) {
        try {
            EmployeeEntity entity = employeeRepository.findByIdAndActiveIsTrue(employeeId)
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

    public EmployeeResponse update(Principal authentication, EmployeeUpdateRequest request) {
        try {
            EmployeeEntity entity = employeeRepository.findByIdAndActiveIsTrue("")
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

    public void deactivate(String employeeId) {
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
            log.error(Message.EMPLOYEE_ERROR_DELETE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.EMPLOYEE_ERROR_DELETE.get(), ex);
        }
    }

    public EmployeeResponse getById(Principal authentication, String employeeId) {
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

    public EmployeeResponse get(Principal authentication) {
        try {
//            CompanyEntity entity = companyRepository.findByIdAndActiveIsTrue(companyId)
//                    .orElseThrow(GenericNotFoundException::new);
//
//            return convert.entityIntoResponse(entity);
            return null;

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
}
