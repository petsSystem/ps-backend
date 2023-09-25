package br.com.petshop.system.access.service;

import br.com.petshop.exception.GenericAlreadyRegisteredException;
import br.com.petshop.exception.GenericNotFoundException;
import br.com.petshop.system.access.model.dto.request.AccessGroupCreateRequest;
import br.com.petshop.system.access.model.dto.request.AccessGroupUpdateRequest;
import br.com.petshop.system.access.model.dto.response.AccessGroupResponse;
import br.com.petshop.system.access.model.entity.AccessGroupEntity;
import br.com.petshop.system.access.repository.AccessGroupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccessGroupService {

    Logger log = LoggerFactory.getLogger(AccessGroupService.class);
    @Autowired private AccessGroupRepository accessGroupRepository;
    @Autowired private AccessConverterService convert;

    public AccessGroupResponse create (AccessGroupCreateRequest request) {
        try {
            AccessGroupEntity accessGroupEntity = accessGroupRepository.findByName(request.getName())
                    .orElse(null);

            if (accessGroupEntity != null)
                throw new GenericAlreadyRegisteredException("Grupo de acesso já cadastrado.");

            accessGroupEntity = convert.createRequestIntoEntity(request);
            accessGroupRepository.save(accessGroupEntity);

            return convert.entityIntoResponse(accessGroupEntity);
        } catch (GenericAlreadyRegisteredException ex) {
            log.error("Already Registered: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao cadastrar usuário. Tente novamente mais tarde.", ex);
        }
    }

    public AccessGroupResponse update (Principal authentication, String accessGroupId, AccessGroupUpdateRequest request) {
        try {
            AccessGroupEntity accessGroupEntity = accessGroupRepository.findById(accessGroupId)
                    .orElseThrow(GenericNotFoundException::new);
            AccessGroupEntity newEntity = convert.updateRequestIntoEntity(request, accessGroupEntity);

            accessGroupRepository.save(newEntity);
            return convert.entityIntoResponse(newEntity);
        } catch (GenericNotFoundException ex) {
            log.error("Not found: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Grupo de acesso não encontrado.", ex);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao atualizar dados do usuário. Tente novamente mais tarde.", ex);
        }
    }

    public List<AccessGroupResponse> findAll (Principal authentication) {
        try {
            List<AccessGroupEntity> all = accessGroupRepository.findAll();
            if (all.isEmpty())
                throw new GenericNotFoundException("Não há grupo de acesso cadastrado.");
            return all.stream()
                    .map(m -> convert.entityIntoResponse(m))
                    .collect(Collectors.toList());
        } catch (GenericNotFoundException ex) {
            log.error("Not found: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao recuperar grupos de acesso do sistema. Tente novamente mais tarde.", ex);
        }
    }

    public void delete (String accessGroupId) {
        try {
            AccessGroupEntity accessGroupEntity = accessGroupRepository.findById(accessGroupId)
                    .orElseThrow(GenericNotFoundException::new);
            accessGroupRepository.delete(accessGroupEntity);
        } catch (Exception ex) {
            log.error("Bad Request: " + ex.getMessage());
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Erro ao excluir grupo de acesso do sistema. Tente novamente mais tarde.", ex);
        }
    }
}
