package br.com.petshop.schedule.service;

import br.com.petshop.commons.exception.GenericAlreadyRegisteredException;
import br.com.petshop.commons.exception.GenericNotFoundException;
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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ScheduleService {
    private Logger log = LoggerFactory.getLogger(ScheduleService.class);
    @Autowired private ScheduleRepository repository;
    @Autowired private ScheduleConverterService converter;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ScheduleSpecification specification;
    @Autowired private SysUserService userService;

    public ScheduleEntity create(ScheduleEntity entity) {
        Optional<ScheduleEntity> schedule = repository
                .findByUserIdAndProductId(entity.getUserId(), entity.getProductId());

        if (schedule.isPresent())
            throw new GenericAlreadyRegisteredException();

        UserEntity userEntity = userService.findById(entity.getUserId());
        entity.setName(userEntity.getName());

        return save(entity);
    }

    public ScheduleEntity save(ScheduleEntity entity) {
        return repository.save(entity);
    }

    public ScheduleEntity activate (UUID scheduleId, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        ScheduleEntity entity = findById(scheduleId);

        entity = applyPatch(patch, entity);

        return repository.save(entity);
    }

    public ScheduleEntity updateById(UUID scheduleId, ScheduleEntity request) {
        ScheduleEntity entity = findById(scheduleId);

        entity = converter.updateRequestIntoEntity(request, entity);

        return repository.save(entity);
    }

    private ScheduleEntity applyPatch(JsonPatch patch, ScheduleEntity entity) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
        return objectMapper.treeToValue(patched, ScheduleEntity.class);
    }

    public ScheduleEntity findById(UUID scheduleId) {
        return repository.findById(scheduleId)
                .orElseThrow(GenericNotFoundException::new);
    }

    public List<ScheduleEntity> findAllByProductId(UUID productId) {
        return repository.findAllByProductId(productId);
    }
}
