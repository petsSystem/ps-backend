package br.com.petshop.appointment.service;

import br.com.petshop.appointment.model.entity.AppointmentEntity;
import br.com.petshop.appointment.repository.AppointmentRepository;
import br.com.petshop.appointment.repository.AppointmentSpecification;
import br.com.petshop.commons.exception.GenericAlreadyRegisteredException;
import br.com.petshop.commons.exception.GenericNotFoundException;
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
public class AppointmentService {
    private Logger log = LoggerFactory.getLogger(AppointmentService.class);
    @Autowired private AppointmentRepository repository;
    @Autowired private AppointmentConverterService converter;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private AppointmentSpecification specification;
    @Autowired private SysUserService userService;

//    public AppointmentEntity create(AppointmentEntity entity) {
//        Optional<AppointmentEntity> schedule = repository
//                .findByUserIdAndProductId(entity.getUserId(), entity.getProductId());
//
//        if (schedule.isPresent())
//            throw new GenericAlreadyRegisteredException();
//
//        UserEntity userEntity = userService.findById(entity.getUserId());
//        entity.setName(userEntity.getName());
//
//        return save(entity);
//    }
//
//    public AppointmentEntity save(AppointmentEntity entity) {
//        return repository.save(entity);
//    }
//
//    public AppointmentEntity activate (UUID scheduleId, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
//        AppointmentEntity entity = findById(scheduleId);
//
//        entity = applyPatch(patch, entity);
//
//        return repository.save(entity);
//    }
//
//    public AppointmentEntity updateById(UUID scheduleId, AppointmentEntity request) {
//        AppointmentEntity entity = findById(scheduleId);
//
//        entity = converter.updateRequestIntoEntity(request, entity);
//
//        return repository.save(entity);
//    }
//
//    private AppointmentEntity applyPatch(JsonPatch patch, AppointmentEntity entity) throws JsonPatchException, JsonProcessingException {
//        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
//        return objectMapper.treeToValue(patched, AppointmentEntity.class);
//    }
//
//    public AppointmentEntity findById(UUID scheduleId) {
//        return repository.findById(scheduleId)
//                .orElseThrow(GenericNotFoundException::new);
//    }
//
//    public List<AppointmentEntity> findAllByProductId(UUID productId) {
//        return repository.findAllByProductId(productId);
//    }
}
