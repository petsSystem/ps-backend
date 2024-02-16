package br.com.petshop.customer.service.sys;

import br.com.petshop.commons.exception.GenericAlreadyRegisteredException;
import br.com.petshop.commons.exception.GenericNotFoundException;
import br.com.petshop.customer.model.entity.CustomerEntity;
import br.com.petshop.customer.model.enums.AppStatus;
import br.com.petshop.customer.repository.CustomerRepository;
import br.com.petshop.customer.repository.CustomerSpecification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerSysService {
    private Logger log = LoggerFactory.getLogger(CustomerSysService.class);
    @Autowired private CustomerRepository repository;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private CustomerSpecification specification;

    public CustomerEntity create(CustomerEntity request) {

        CustomerEntity entity = repository.findByCpfAndActiveIsTrue(request.getCpf()).orElse(null);

        if (entity != null) {
            if (entity.getCompanyIds().contains(request.getCompanyIds().get(0)))
                throw new GenericAlreadyRegisteredException();
            else {
                entity.getCompanyIds().add(request.getCompanyIds().get(0));
                return repository.save(entity);
            }
        }

        request.setAppStatus(AppStatus.PENDING);
        request.setUsername(entity.getCpf());
        return repository.save(request);
    }

    public CustomerEntity updateById(CustomerEntity entity) {
        return repository.save(entity);
    }

    public CustomerEntity findById(UUID id) {
        return repository.findById(id)
                .orElseThrow(GenericNotFoundException::new);
    }

    public CustomerEntity associateCompanyId (CustomerEntity entity, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        String companyIdString = ((ObjectNode) patched).get("companyIds").get(0).toString();
        UUID companyId = UUID.fromString(companyIdString.replaceAll("\"", ""));

        if (!entity.getFavorites().contains(companyId))
            entity.getFavorites().add(companyId);

        if (!entity.getCompanyIds().contains(companyId))
            entity.getCompanyIds().add(companyId);

        return repository.save(entity);
    }

    public Page<CustomerEntity> findAllByCompanyId(UUID companyId, Pageable pageable) {
        Specification<CustomerEntity> filters = specification.filter(companyId);
        return repository.findAll(filters, pageable);
    }
}
