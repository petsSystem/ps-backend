package br.com.petshop.system.company.service;

import br.com.petshop.system.company.model.dto.request.CompanyCreateRequest;
import br.com.petshop.system.company.model.dto.request.CompanyUpdateRequest;
import br.com.petshop.system.company.model.dto.response.CompanyResponse;
import br.com.petshop.system.company.model.entity.CompanyEntity;
import br.com.petshop.system.company.repository.CompanyRepository;
import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Optional;

@Service
public class CompanyService {
    Logger log = LoggerFactory.getLogger(CompanyService.class);
    @Autowired private CompanyRepository companyRepository;
    @Autowired private CompanyConverterService convert;

    public CompanyResponse create(Principal authentication, CompanyCreateRequest request) {
        try {
            Optional<CompanyEntity> company = companyRepository.findByCnpjAndActiveIsTrue(request.getCnpj());
            if (company.isPresent())
                throw new GenericAlreadyRegisteredException("Empresa já cadastrada no sistema.");

            CompanyEntity companyEntity = convert.createRequestIntoEntity(request);

            companyEntity = companyRepository.save(companyEntity);

            return convert.entityIntoResponse(companyEntity);

        } catch (GenericAlreadyRegisteredException ex) {
            log.error("Already registered: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao cadastrar empresa. Tente novamente mais tarde.", ex);
        }
    }

    public CompanyResponse update(Principal authentication, String companyId, CompanyUpdateRequest request) {
        try {
            CompanyEntity entity = companyRepository.findByIdAndActiveIsTrue(companyId)
                    .orElseThrow(GenericNotFoundException::new);

            entity = convert.updateRequestIntoEntity(request, entity);
            entity = companyRepository.save(entity);

            return convert.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error("Company not found: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao atualizar dados da empresa. Tente novamente mais tarde.", ex);
        }
    }

    public void deactivate(String companyId) {
        try {
            CompanyEntity entity = companyRepository.findByIdAndActiveIsTrue(companyId)
                    .orElseThrow(GenericNotFoundException::new);
            entity.setActive(false);
            companyRepository.save(entity);

        } catch (GenericNotFoundException ex) {
            log.error("Company not found: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Cadastro de empresa não encontrado.", ex);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao excluir dados da empresa. Tente novamente mais tarde.", ex);
        }
    }

    public CompanyResponse getByCompanyId(Principal authentication, String companyId) {
        try {
            CompanyEntity entity = companyRepository.findByIdAndActiveIsTrue(companyId)
                    .orElseThrow(GenericNotFoundException::new);

            return convert.entityIntoResponse(entity);

        } catch (GenericNotFoundException ex) {
            log.error("Company not found: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao recuperar dados da empresa. Tente novamente mais tarde.", ex);
        }
    }

    public CompanyResponse get(Principal authentication) {
        try {
//            CompanyEntity entity = companyRepository.findByIdAndActiveIsTrue(companyId)
//                    .orElseThrow(GenericNotFoundException::new);
//
//            return convert.entityIntoResponse(entity);
            return null;

        } catch (GenericNotFoundException ex) {
            log.error("Company not found: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao recuperar dados da empresa. Tente novamente mais tarde.", ex);
        }
    }
}
