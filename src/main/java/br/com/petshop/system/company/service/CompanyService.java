package br.com.petshop.system.company.service;

import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.system.company.model.dto.enums.Message;
import br.com.petshop.system.company.model.dto.request.CompanyCreateRequest;
import br.com.petshop.system.company.model.dto.request.CompanyUpdateRequest;
import br.com.petshop.system.company.model.dto.response.CompanyResponse;
import br.com.petshop.system.company.model.dto.response.CompanySummaryResponse;
import br.com.petshop.system.company.model.entity.CompanyEntity;
import br.com.petshop.system.company.repository.CompanyRepository;
import br.com.petshop.utils.PetGeometry;
import org.locationtech.jts.geom.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CompanyService {
    Logger log = LoggerFactory.getLogger(CompanyService.class);
    @Autowired private CompanyRepository companyRepository;
    @Autowired private CompanyConverterService convert;

    @Autowired private PetGeometry geometry;

    public CompanyResponse create(Principal authentication, CompanyCreateRequest request) {
        try {
            Optional<CompanyEntity> company = companyRepository.findByCnpjAndActiveIsTrue(request.getCnpj());
            if (company.isPresent())
                throw new GenericAlreadyRegisteredException(Message.COMPANY_ALREADY_REGISTERED.get());

            CompanyEntity companyEntity = convert.createRequestIntoEntity(request);
            companyEntity.setGeom((Point)
                    geometry.getPoint(companyEntity.getAddressLat(), companyEntity.getAddressLon()));

            companyEntity = companyRepository.save(companyEntity);

            return convert.entityIntoResponse(companyEntity);

        } catch (GenericAlreadyRegisteredException ex) {
            log.error("Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error(Message.COMPANY_ERROR_CREATE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.COMPANY_ERROR_CREATE.get(), ex);
        }
    }

    public List<CompanySummaryResponse> findAround(Point p, Double distance) {
        List<CompanyEntity> companies = companyRepository.findNearWithinDistance(p, distance);
        return companies.stream()
                .map(c -> {
                    CompanySummaryResponse response = convert.entityIntoAppResponse(c);
                    Double dist = companyRepository.getDistance(p, c.getId());
                    response.setDistance(dist);
                    return response;
                })
                .collect(Collectors.toList());
    }

//    public List<CompanyEntity> findAround(Double lat, Double lon, Double distance){
//        log.info("Looking for city around ({},{}) withing {} meters", lat, lon, distance);
//        Point p = factory.createPoint(new Coordinate(lon, lat));
//        return companyRepository.findNearWithinDistance(p, distance);
//    }

    public CompanyEntity findByIdAndActiveIsTrue(String companyId) {
        return companyRepository.findByIdAndActiveIsTrue(companyId)
                .orElseThrow(GenericNotFoundException::new);
    }

    public CompanyResponse updateById(String companyId, CompanyUpdateRequest request) {
        try {
            CompanyEntity entity = findByIdAndActiveIsTrue(companyId);

            entity = convert.updateRequestIntoEntity(request, entity);
            entity = companyRepository.save(entity);

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

    public CompanyResponse update(Principal authentication, CompanyUpdateRequest request) {
        try {
            CompanyEntity entity = companyRepository.findByIdAndActiveIsTrue(null)
                    .orElseThrow(GenericNotFoundException::new);

            entity = convert.updateRequestIntoEntity(request, entity);
            entity = companyRepository.save(entity);

            return convert.entityIntoResponse(entity);

        } catch (Exception ex) {
            log.error(Message.COMPANY_ERROR_UPDATE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.COMPANY_ERROR_UPDATE.get(), ex);
        }
    }

    public CompanyResponse getByCompanyId(Principal authentication, String companyId) {
        try {
            CompanyEntity entity = findByIdAndActiveIsTrue(companyId);

            return convert.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.COMPANY_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.COMPANY_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.COMPANY_ERROR_GET.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.COMPANY_ERROR_GET.get(), ex);
        }
    }

    public CompanyResponse get(Principal authentication) {
        try {
//            CompanyEntity entity = findById(companyId);
//
//            return convert.entityIntoResponse(entity);
            return null;

        } catch (Exception ex) {
            log.error(Message.COMPANY_ERROR_GET.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.COMPANY_ERROR_GET.get(), ex);
        }
    }

    public void deactivate(String companyId) {
        try {
            CompanyEntity entity = findByIdAndActiveIsTrue(companyId);
            entity.setActive(false);
            companyRepository.save(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.COMPANY_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.COMPANY_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.COMPANY_ERROR_DEACTIVATE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.COMPANY_ERROR_DEACTIVATE.get(), ex);
        }
    }

    public void activate(String companyId) {
        try {
            CompanyEntity entity = companyRepository.findById(companyId)
                    .orElseThrow(GenericNotFoundException::new);
            entity.setActive(true);
            companyRepository.save(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.COMPANY_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.COMPANY_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.COMPANY_ERROR_ACTIVATE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.COMPANY_ERROR_ACTIVATE.get(), ex);
        }
    }

    public void delete(String companyId) {
        try {
            CompanyEntity entity = companyRepository.findById(companyId)
                    .orElseThrow(GenericNotFoundException::new);
            companyRepository.delete(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.COMPANY_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.COMPANY_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.COMPANY_ERROR_DELETE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.COMPANY_ERROR_DELETE.get(), ex);
        }
    }
}
