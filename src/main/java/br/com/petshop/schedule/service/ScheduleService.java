package br.com.petshop.schedule.service;

import br.com.petshop.commons.exception.GenericAlreadyRegisteredException;
import br.com.petshop.commons.exception.GenericNotFoundException;
import br.com.petshop.schedule.model.dto.request.ScheduleFilterRequest;
import br.com.petshop.schedule.model.entity.ScheduleEntity;
import br.com.petshop.schedule.repository.ScheduleRepository;
import br.com.petshop.schedule.repository.ScheduleSpecification;
import br.com.petshop.user.model.entity.UserEntity;
import br.com.petshop.user.service.SysUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ScheduleService {
    private Logger log = LoggerFactory.getLogger(ScheduleService.class);
    @Autowired private ScheduleRepository repository;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ScheduleSpecification specification;
    @Autowired private SysUserService userService;

    public ScheduleEntity create(ScheduleEntity entity) {
        Optional<ScheduleEntity> schedule = repository
                .findByIdAndCategoryId(entity.getUserId(), entity.getCategoryId());

        if (schedule.isPresent())
            throw new GenericAlreadyRegisteredException();

        UserEntity userEntity = userService.findById(entity.getUserId());
        entity.setName(userEntity.getName());

        return repository.save(entity);
    }

    public ScheduleEntity update(ScheduleEntity entity) {
        return repository.save(entity);
    }

    public ScheduleEntity activate (ScheduleEntity entity, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        entity = objectMapper.treeToValue(patched, ScheduleEntity.class);

        return repository.save(entity);
    }

    public List<ScheduleEntity> findAllByFilter(ScheduleFilterRequest filter) {
        Specification<ScheduleEntity> filters = specification.filter(filter);
        return repository.findAll(filters);
    }

    public ScheduleEntity findById(UUID scheduleId) {
        return repository.findById(scheduleId)
                .orElseThrow(GenericNotFoundException::new);
    }

    public ScheduleEntity findByUserId(UUID userId) {
        return repository.findByUserId(userId)
                .orElseThrow(GenericNotFoundException::new);
    }
}
