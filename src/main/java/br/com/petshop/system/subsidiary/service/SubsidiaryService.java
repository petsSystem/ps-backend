package br.com.petshop.system.subsidiary.service;

import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.system.company.model.entity.CompanyEntity;
import br.com.petshop.system.company.service.CompanyService;
import br.com.petshop.system.subsidiary.model.dto.request.SubsidiaryCreateRequest;
import br.com.petshop.system.subsidiary.model.dto.request.SubsidiaryUpdateRequest;
import br.com.petshop.system.subsidiary.model.dto.response.SubsidiaryResponse;
import br.com.petshop.system.subsidiary.model.entity.SubsidiaryEntity;
import br.com.petshop.system.subsidiary.model.enums.Message;
import br.com.petshop.system.subsidiary.repository.SubsidiaryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Service
public class SubsidiaryService {
    Logger log = LoggerFactory.getLogger(SubsidiaryService.class);
    @Autowired private SubsidiaryRepository subsidiaryRepository;
    @Autowired private SubsidiaryConverterService convert;

    @Autowired private CompanyService companyService;

    public SubsidiaryResponse create(Principal authentication, SubsidiaryCreateRequest request) {
        try {
            List<SubsidiaryEntity> entities = subsidiaryRepository.findByCompany_id(request.getCompanyId());

            final SubsidiaryEntity[] subsidiaryEntity = {null};

            entities.stream()
                    .filter(s -> s.getName().equalsIgnoreCase(request.getName()))
                    .findAny()
                    .ifPresentOrElse(
                            (error) -> {
                                throw new GenericAlreadyRegisteredException("Loja já cadastrada no sistema.");
                            },
                            () -> {
                                SubsidiaryEntity entity = convert.createRequestIntoEntity(request);
                                CompanyEntity companyEntity = companyService.findByIdAndActiveIsTrue(request.getCompanyId());
                                entity.setCompany(companyEntity);
                                subsidiaryEntity[0] = subsidiaryRepository.save(entity);
                            });

            return convert.entityIntoResponse(subsidiaryEntity[0]);

        } catch (GenericAlreadyRegisteredException ex) {
            log.error("Already registered: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao cadastrar pet. Tente novamente mais tarde.", ex);
        }
    }

    public SubsidiaryEntity findByIdAndActiveIsTrue(String subsidiaryId) {
        return subsidiaryRepository.findByIdAndActiveIsTrue(subsidiaryId)
                .orElseThrow(GenericNotFoundException::new);
    }

    public SubsidiaryResponse updateById(String subsidiaryId, SubsidiaryUpdateRequest request) {
        try {
            SubsidiaryEntity entity = findByIdAndActiveIsTrue(subsidiaryId);

            entity = convert.updateRequestIntoEntity(request, entity);
            entity = subsidiaryRepository.save(entity);

            return convert.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.SUBSIDIARY_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.SUBSIDIARY_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.SUBSIDIARY_ERROR_UPDATE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.SUBSIDIARY_ERROR_UPDATE.get(), ex);
        }
    }

    public SubsidiaryResponse update(Principal authentication, SubsidiaryUpdateRequest request) {
        try {
            SubsidiaryEntity entity = findByIdAndActiveIsTrue(null);

            entity = convert.updateRequestIntoEntity(request, entity);
            entity = subsidiaryRepository.save(entity);

            return convert.entityIntoResponse(entity);

        } catch (Exception ex) {
            log.error(Message.SUBSIDIARY_ERROR_UPDATE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.SUBSIDIARY_ERROR_UPDATE.get(), ex);
        }
    }

    public SubsidiaryResponse getByCompanyId(Principal authentication, String companyId) {
        try {
            SubsidiaryEntity entity = findByIdAndActiveIsTrue(companyId);

            return convert.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.SUBSIDIARY_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.SUBSIDIARY_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.SUBSIDIARY_ERROR_GET.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.SUBSIDIARY_ERROR_GET.get(), ex);
        }
    }

    public Set<SubsidiaryResponse> get(Principal authentication) {
        try {
//            SubsidiaryEntity entity = findById(companyId);
//
//            return convert.entityIntoResponse(entity);
            return null;

        } catch (Exception ex) {
            log.error(Message.SUBSIDIARY_ERROR_GET.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.SUBSIDIARY_ERROR_GET.get(), ex);
        }
    }

    public void deactivate(String subsidiaryId) {
        try {
            SubsidiaryEntity entity = findByIdAndActiveIsTrue(subsidiaryId);
            entity.setActive(false);
            subsidiaryRepository.save(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.SUBSIDIARY_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.SUBSIDIARY_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.SUBSIDIARY_ERROR_DEACTIVATE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.SUBSIDIARY_ERROR_DEACTIVATE.get(), ex);
        }
    }

    public void activate(String subsidiaryId) {
        try {
            SubsidiaryEntity entity = subsidiaryRepository.findById(subsidiaryId)
                    .orElseThrow(GenericNotFoundException::new);
            entity.setActive(true);
            subsidiaryRepository.save(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.SUBSIDIARY_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.SUBSIDIARY_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.SUBSIDIARY_ERROR_ACTIVATE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.SUBSIDIARY_ERROR_ACTIVATE.get(), ex);
        }
    }

    public void delete(String subsidiaryId) {
        try {
            SubsidiaryEntity entity = subsidiaryRepository.findById(subsidiaryId)
                    .orElseThrow(GenericNotFoundException::new);
            subsidiaryRepository.delete(entity);

        } catch (GenericNotFoundException ex) {
            log.error(Message.SUBSIDIARY_NOT_FOUND.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, Message.SUBSIDIARY_NOT_FOUND.get(), ex);
        } catch (Exception ex) {
            log.error(Message.SUBSIDIARY_ERROR_DELETE.get() + " Error: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, Message.SUBSIDIARY_ERROR_DELETE.get(), ex);
        }
    }
}
