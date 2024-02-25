package br.com.petshop.profile.service;

import br.com.petshop.commons.exception.GenericAlreadyRegisteredException;
import br.com.petshop.commons.exception.GenericNotFoundException;
import br.com.petshop.profile.model.entity.ProfileEntity;
import br.com.petshop.profile.repository.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Classe responsável pelos serviços de perfis dos usuários do sistema web.
 */
@Service
public class ProfileService {

    private Logger log = LoggerFactory.getLogger(ProfileService.class);
    @Autowired private ProfileRepository repository;

    /**
     * Método de criação de perfil de usuário do sistema web.
     * @param request - entidade de perfil
     * @return - entidade de perfil
     */
    public ProfileEntity create (ProfileEntity request) {
        ProfileEntity profileEntity = repository.findByName(request.getName())
                .orElse(null);

        if (profileEntity != null)
            throw new GenericAlreadyRegisteredException();

        return save(request);
    }

    /**
     * Método que salva a entidade de perfil de usuário do sistema web.
     * @param entity - entidade de perfil
     * @return - entidade de perfil
     */
    public ProfileEntity save(ProfileEntity entity) {
        return repository.save(entity);
    }

    /**
     * Método que retorna dados de perfil de usuário do sistema web, através da informação do id.
     * @param profileId
     * @return - entidade de perfil
     */
    public ProfileEntity findById(UUID profileId) {
        return repository.findById(profileId)
                .orElseThrow(GenericNotFoundException::new);
    }

    /**
     * Método que retorna labels dos perfis de usuários do sistema web.
     * @return - lista de entidades de perfil
     */
    public List<ProfileEntity> findAllLabels() {
        return repository.findAll();
    }

    /**
     * Método que retorna dados de perfis de usuários do sistema web.
     * @param pageable - dados de paginação
     * @return - lista de entidades de perfil
     */
    public Page<ProfileEntity> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
