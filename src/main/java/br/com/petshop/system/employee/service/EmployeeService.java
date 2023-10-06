package br.com.petshop.system.employee.service;

import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.system.company.model.entity.CompanyEntity;
import br.com.petshop.system.company.service.CompanyService;
import br.com.petshop.system.employee.model.dto.request.EmployeeCreateRequest;
import br.com.petshop.system.employee.model.dto.request.EmployeeUpdateRequest;
import br.com.petshop.system.employee.model.dto.response.EmployeeResponse;
import br.com.petshop.system.employee.model.entity.EmployeeEntity;
import br.com.petshop.system.employee.model.enums.EmployeeType;
import br.com.petshop.system.employee.model.enums.Message;
import br.com.petshop.system.employee.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeService {
    Logger log = LoggerFactory.getLogger(EmployeeService.class);
    @Autowired private EmployeeRepository employeeRepository;
    @Autowired private EmployeeConverterService convert;
    @Autowired private CompanyService companyService;

    public EmployeeResponse create(Principal authentication, EmployeeCreateRequest request) {
        try {
            Optional<EmployeeEntity> entity = employeeRepository.findByCpfAndActiveIsTrue(request.getCpf());
            CompanyEntity companyEntity = companyService.findByIdAndActiveIsTrue(request.getCompanyId());
            EmployeeEntity employeeEntity = convert.createRequestIntoEntity(request);

            if (entity.isEmpty()) {
                employeeEntity.setCompanyEmployees(Set.of(companyEntity));
                employeeRepository.save(employeeEntity);

            } else { //associar a nova empresa
                if (request.getType() != EmployeeType.OWNER)
                    throw new GenericAlreadyRegisteredException("Funcionário já cadastrado");

                employeeEntity = entity.get();
                List<CompanyEntity> companies = companyService.findByEmployeeId(employeeEntity.getId());

                Optional<CompanyEntity> optionalCompany = companies.stream()
                        .filter(c -> c.getId().equals(request.getCompanyId()))
                        .findFirst();
                if (optionalCompany.isEmpty()) {
                    companies.add(companyEntity);
                    employeeEntity.setCompanyEmployees(companies.stream().collect(Collectors.toSet()));
                    employeeRepository.save(employeeEntity);
                } else
                    throw new GenericAlreadyRegisteredException("Company already registered for this employee");
            }

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

    public EmployeeResponse updateById(Principal authentication, UUID employeeId, EmployeeUpdateRequest request) {
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
