package br.com.petshop.company.service;

import br.com.petshop.commons.exception.GenericAlreadyRegisteredException;
import br.com.petshop.commons.exception.GenericNotFoundException;
import br.com.petshop.company.model.entity.CompanyEntity;
import br.com.petshop.company.repository.CompanyRepository;
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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CompanyService {
    private Logger log = LoggerFactory.getLogger(CompanyService.class);
    @Autowired private CompanyRepository repository;
    @Autowired private ObjectMapper objectMapper;

    public CompanyEntity create(CompanyEntity entity) {
        Optional<CompanyEntity> company = repository.findByCnpj(entity.getCnpj());
        if (company.isPresent())
            throw new GenericAlreadyRegisteredException();

        return repository.save(entity);
    }

    public CompanyEntity findByIdAndActiveIsTrue(UUID companyId) {
        return repository.findById(companyId)
                .orElseThrow(GenericNotFoundException::new);
    }

    public CompanyEntity updateById(CompanyEntity entity) {
        return repository.save(entity);
    }

    public CompanyEntity activate (CompanyEntity entity, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        entity = objectMapper.treeToValue(patched, CompanyEntity.class);

        return repository.save(entity);
    }

    public Page<CompanyEntity> findAll (Pageable paging) {
            return repository.findAll(paging);
    }

    public Page<CompanyEntity> findByCompanyIds(List<UUID> companyIds) {
        List<CompanyEntity> companies = companyIds.stream()
                .map(c -> {
                    Optional<CompanyEntity> entity = repository.findByIdAndActiveIsTrue(c);
                    if (entity.isPresent())
                        return entity.get();
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new PageImpl<>(companies);
    }

    public CompanyEntity findById(UUID companyId) {
        return repository.findById(companyId)
                .orElseThrow(GenericNotFoundException::new);
    }
}
