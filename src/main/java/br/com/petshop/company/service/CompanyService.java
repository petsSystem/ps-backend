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

/**
 * Classe responsável pelos serviços de lojas/petshops
 */
@Service
public class CompanyService {
    private Logger log = LoggerFactory.getLogger(CompanyService.class);
    @Autowired private CompanyRepository repository;
    @Autowired private ObjectMapper objectMapper;

    /**
     * Método de criação de loja/petshop.
     * @param entity - entidade loja/petshop
     * @return - entidade loja/petshop
     */
    public CompanyEntity create(CompanyEntity entity) {
        Optional<CompanyEntity> company = repository.findByCnpj(entity.getCnpj());
        if (company.isPresent())
            throw new GenericAlreadyRegisteredException();

        return repository.save(entity);
    }

    /**
     * Método de atualização de loja/petshop.
     * @param entity - entidade loja/petshop
     * @return - entidade loja/petshop
     */
    public CompanyEntity updateById(CompanyEntity entity) {
        return repository.save(entity);
    }

    /**
     * Método de ativação/destivação de loja/petshop.
     * @param entity - entidade loja/petshop
     * @param patch - dados de ativação/destivação de loja/petshop
     * @return - entidade loja/petshop
     * @throws JsonPatchException
     * @throws JsonProcessingException
     */
    public CompanyEntity activate (CompanyEntity entity, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        entity = objectMapper.treeToValue(patched, CompanyEntity.class);

        return repository.save(entity);
    }

    /**
     * Método que recupera uma loja/petshop ativa pelo id.
     * @param companyId - id de cadastro da loja/petshop
     * @return - entidade loja/petshop
     */
    public CompanyEntity findByIdAndActiveIsTrue(UUID companyId) {
        return repository.findById(companyId)
                .orElseThrow(GenericNotFoundException::new);
    }

    /**
     * Método que recupera todas as loja/petshop.
     * @param paging - dados de paginação
     * @return - lista de entidade loja/petshop
     */
    public Page<CompanyEntity> findAll (Pageable paging) {
            return repository.findAll(paging);
    }

    /**
     * Método que recupera todas as loja/petshop através de uma lista de ids informado.
     * @param companyIds - ids de cadastro da loja/petshop
     * @param paging - dados de paginação
     * @return - lista de entidade loja/petshop
     */
    public Page<CompanyEntity> findByCompanyIds(List<UUID> companyIds, Pageable paging) {
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

    /**
     * Método que recupera uma loja/petshop pelo id.
     * @param companyId - id de cadastro da loja/petshop
     * @return - entidade loja/petshop
     */
    public CompanyEntity findById(UUID companyId) {
        return repository.findById(companyId)
                .orElseThrow(GenericNotFoundException::new);
    }
}
