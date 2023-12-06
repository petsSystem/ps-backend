package br.com.petshop.system.employee.service;

import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.system.company.model.entity.CompanyEntity;
import br.com.petshop.system.company.service.CompanyService;
import br.com.petshop.system.employee.model.dto.request.EmployeeFilterRequest;
import br.com.petshop.system.employee.model.dto.response.EmployeeResponse;
import br.com.petshop.system.employee.model.entity.EmployeeEntity;
import br.com.petshop.system.employee.repository.EmployeeRepository;
import br.com.petshop.system.employee.repository.EmployeeSpecification;
import br.com.petshop.system.user.service.SysUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.JsonPatchOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
    @Autowired private SysUserService sysUserService;
    @Autowired private ObjectMapper objectMapper;

    public EmployeeEntity create(EmployeeEntity request) {
        Optional<EmployeeEntity> entity = employeeRepository.findByCpfAndActiveIsTrue(request.getCpf());

        if (entity.isPresent())
            throw new GenericAlreadyRegisteredException();

        for(UUID companyId : request.getCompanyIds())
            companyService.findByIdAndActiveIsTrue(companyId);

        return employeeRepository.save(request);
    }

    public EmployeeEntity partialUpdate(UUID employeeId, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        EmployeeEntity entity = employeeRepository.findById(employeeId)
                .orElseThrow(GenericNotFoundException::new);

        entity = applyPatch(patch, entity);

        return employeeRepository.save(entity);
    }

    private EmployeeEntity applyPatch(JsonPatch patch, EmployeeEntity entity) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        return objectMapper.treeToValue(patched, EmployeeEntity.class);
    }

    public  Page<EmployeeResponse> get(Pageable pageable, EmployeeFilterRequest filter) {
            Specification<EmployeeEntity> filters = specification.filter(filter);

            Page<EmployeeEntity> entities = employeeRepository.findAll(filters, pageable);

            List<EmployeeResponse> response = entities.stream()
                    .map(c -> convert.entityIntoResponse(c))
                    .collect(Collectors.toList());

            return new PageImpl<>(response);
    }

    public EmployeeEntity findById(UUID employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(GenericNotFoundException::new);
    }

    public EmployeeEntity findByIdAndActiveIsTrue(UUID employeeId) {
        return employeeRepository.findByIdAndActiveIsTrue(employeeId)
                .orElseThrow(GenericNotFoundException::new);
    }

    public EmployeeEntity updateById(EmployeeEntity request, EmployeeEntity entity) {
        entity = convert.updateRequestIntoEntity(request, entity);

        return employeeRepository.save(entity);
    }

    public EmployeeEntity save(EmployeeEntity entity) {
        return employeeRepository.save(entity);
    }

    public void delete(UUID employeeId) {
        EmployeeEntity entity = employeeRepository.findById(employeeId)
                .orElseThrow(GenericNotFoundException::new);
        employeeRepository.delete(entity);
    }

    public EmployeeEntity findByIdAndActive(UUID employeeId) {
        Optional<EmployeeEntity> entity = employeeRepository.findByIdAndActiveIsTrue(employeeId);
        if (entity.isEmpty())
            throw new GenericNotFoundException();
        return entity.get();
    }
}
