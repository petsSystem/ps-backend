package br.com.petshop.system.company.service;

import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.system.company.model.dto.response.CompanySummaryResponse;
import br.com.petshop.system.company.model.entity.CompanyEntity;
import br.com.petshop.system.company.repository.CompanyRepository;
import br.com.petshop.utils.PetGeometry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CompanyService {
    Logger log = LoggerFactory.getLogger(CompanyService.class);
    @Autowired private CompanyRepository companyRepository;
    @Autowired private CompanyConverterService convert;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private PetGeometry geometry;

    public CompanyEntity create(CompanyEntity request) {
        Optional<CompanyEntity> company = companyRepository.findByCnpj(request.getCnpj());
        if (company.isPresent())
            throw new GenericAlreadyRegisteredException();

        request.setGeom((Point)
                geometry.getPoint(request.getAddress().getLat(), request.getAddress().getLon()));

        return companyRepository.save(request);
    }

    public CompanyEntity partialUpdate(UUID companyId, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        CompanyEntity entity = companyRepository.findById(companyId)
                .orElseThrow(GenericNotFoundException::new);

        entity = applyPatch(patch, entity);

        return companyRepository.save(entity);
    }

    public Page<CompanyEntity> findAll (Pageable paging) {
            return companyRepository.findAll(paging);
    }

    public Page<CompanyEntity> findByEmployeeId(UUID employeeId, Pageable paging) {
        return companyRepository.findCompaniesFromEmployeeId(employeeId, paging);
    }

    public CompanyEntity findByIdAndActiveIsTrue(UUID companyId) {
        return companyRepository.findByIdAndActiveIsTrue(companyId)
                .orElseThrow(GenericNotFoundException::new);
    }

    public CompanyEntity updateById(UUID companyId, CompanyEntity request) {
        CompanyEntity entity = findByIdAndActiveIsTrue(companyId);

        entity = convert.updateRequestIntoEntity(request, entity);
        return companyRepository.save(entity);
    }

    public void delete(UUID companyId) {
        CompanyEntity entity = companyRepository.findById(companyId)
                .orElseThrow(GenericNotFoundException::new);
        companyRepository.delete(entity);
    }

    private CompanyEntity applyPatch(JsonPatch patch, CompanyEntity entity) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        return objectMapper.treeToValue(patched, CompanyEntity.class);
    }

    public List<CompanySummaryResponse> findAround(Point p, Double distance) {
        List<CompanyEntity> companies = companyRepository.findNearWithinDistance(p, distance);
        if (companies.isEmpty())
            throw new GenericNotFoundException();
        return companies.stream()
                .map(c -> {
                    CompanySummaryResponse response = convert.entityIntoAppResponse(c);
                    Double dist = companyRepository.getDistance(p, c.getId());
                    response.setDistance(dist);
                    return response;
                })
                .collect(Collectors.toList());
    }
}
