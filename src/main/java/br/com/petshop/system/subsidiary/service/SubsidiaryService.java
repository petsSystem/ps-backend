package br.com.petshop.system.subsidiary.service;

import br.com.petshop.system.company.service.CompanyConverterService;
import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.system.subsidiary.model.dto.request.SubsidiaryCreateRequest;
import br.com.petshop.system.subsidiary.model.dto.request.SubsidiaryUpdateRequest;
import br.com.petshop.system.subsidiary.model.dto.response.SubsidiaryResponse;
import br.com.petshop.system.subsidiary.repository.SubsidiaryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.Set;

@Service
public class SubsidiaryService {
    Logger log = LoggerFactory.getLogger(SubsidiaryService.class);
    @Autowired private SubsidiaryRepository subsidiaryRepository;
    @Autowired private CompanyConverterService convert;

    public SubsidiaryResponse create(Principal authentication, SubsidiaryCreateRequest request) {
        try {
//            List<SubsidiaryEntity> pets = subsidiaryRepository.findByAppUser_EmailAndActiveIsTrue(authentication.getName());
//
//            final SubsidiaryEntity[] petEntity = {null};
//
//            pets.stream()
//                    .filter(p -> p.getName().equalsIgnoreCase(request.getName()))
//                    .findAny()
//                    .ifPresentOrElse(
//                            (error) -> {
//                                throw new GenericAlreadyRegisteredException("Pet " + request.getName() + " já cadastrado(a) no sistema.");
//                            },
//                            () -> {
//                                PetEntity entity = convert.convertPetCreateRequestIntoEntity(request);
//                                AppUserEntity userEntity = appUserService.findByEmail(authentication.getName());
//                                entity.setAppUser(userEntity);
//                                petEntity[0] = petRepository.save(entity);
//                            });
//
//            return convert.convertPetEntityIntoResponse(petEntity[0]);
            return null;

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

    public SubsidiaryResponse update(Principal authentication, String petId, SubsidiaryUpdateRequest request) {
        try {
//            SubsidiaryEntity entity = subsidiaryRepository.findByIdAndActiveIsTrue(petId)
//                    .orElseThrow(GenericNotFoundException::new);
//
//            entity = convert.convertPetUpdateRequestIntoEntity(request, entity);
//            petRepository.save(entity);
//
//            return convert.convertPetEntityIntoResponse(entity);
            return null;

        } catch (GenericNotFoundException ex) {
            log.error("Pet not found: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao atualizar dados do pet. Tente novamente mais tarde.", ex);
        }
    }

    public void deactivate(String petId) {
        try {
//            SubsidiaryEntity entity = subsidiaryRepository.findByIdAndActiveIsTrue(petId)
//                    .orElseThrow(GenericNotFoundException::new);
//            entity.setActive(false);
//            petRepository.save(entity);

        } catch (GenericNotFoundException ex) {
            log.error("Pet not found: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Cadastro de pet não encontrado.", ex);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao excluir dados do pet. Tente novamente mais tarde.", ex);
        }
    }

    public Set<SubsidiaryResponse> get(Principal authentication) {
        try {
//            List<SubsidiaryEntity> pets = subsidiaryRepository.findByAppUser_EmailAndActiveIsTrue(authentication.getName());
//
//            return pets.stream()
//                    .map(p -> convert.entityIntoResponse(p))
//                    .collect(Collectors.toSet());
            return null;

        } catch (GenericNotFoundException ex) {
            log.error("Pet not found: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao recuperar dados do pet. Tente novamente mais tarde.", ex);
        }
    }

    public SubsidiaryResponse getBySubsidiaryId(Principal authentication, String subsidiaryId) {
        try {
//            List<SubsidiaryEntity> pets = subsidiaryRepository.findByAppUser_EmailAndActiveIsTrue(authentication.getName());
//
//            return pets.stream()
//                    .map(p -> convert.entityIntoResponse(p))
//                    .collect(Collectors.toSet());
            return null;

        } catch (GenericNotFoundException ex) {
            log.error("Pet not found: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao recuperar dados do pet. Tente novamente mais tarde.", ex);
        }
    }
}
