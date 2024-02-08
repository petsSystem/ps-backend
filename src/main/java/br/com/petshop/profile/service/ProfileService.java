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

@Service
public class ProfileService {

    private Logger log = LoggerFactory.getLogger(ProfileService.class);
    @Autowired private ProfileRepository repository;

    public ProfileEntity create (ProfileEntity request) {
        ProfileEntity profileEntity = repository.findByName(request.getName())
                .orElse(null);

        if (profileEntity != null)
            throw new GenericAlreadyRegisteredException();

        return save(request);
    }

    public ProfileEntity save(ProfileEntity entity) {
        return repository.save(entity);
    }

    public ProfileEntity findById(UUID profileId) {
        return repository.findById(profileId)
                .orElseThrow(GenericNotFoundException::new);
    }

    public List<ProfileEntity> findAllLabels() {
        return repository.findAll();
    }

    public Page<ProfileEntity> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
