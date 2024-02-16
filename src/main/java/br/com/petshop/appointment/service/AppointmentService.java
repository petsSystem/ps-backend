package br.com.petshop.appointment.service;

import br.com.petshop.appointment.model.dto.request.AppointmentFilterRequest;
import br.com.petshop.appointment.model.dto.request.AppointmentStatusRequest;
import br.com.petshop.appointment.model.entity.AppointmentEntity;
import br.com.petshop.appointment.model.enums.Status;
import br.com.petshop.appointment.repository.AppointmentRepository;
import br.com.petshop.appointment.repository.AppointmentSpecification;
import br.com.petshop.commons.exception.GenericNotFoundException;
import br.com.petshop.schedule.model.entity.ScheduleEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AppointmentService {
    private Logger log = LoggerFactory.getLogger(AppointmentService.class);
    @Autowired private AppointmentRepository repository;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private AppointmentSpecification specification;

    public AppointmentEntity create(AppointmentEntity entity) {
        return repository.save(entity);
    }

    public AppointmentEntity updateById(AppointmentEntity entity) {
        return repository.save(entity);
    }

    public AppointmentEntity findById(UUID appointmentId) {
        return repository.findById(appointmentId)
                .orElseThrow(GenericNotFoundException::new);
    }

    public AppointmentEntity setStatus(AppointmentEntity entity, AppointmentStatusRequest request) {
        entity.setStatus(request.getStatus());

        if (request.getStatus() == Status.CANCELLED) {
            entity.setComments(request.getComments());
            entity.setActive(false);
        }

        return repository.save(entity);
    }

    public List<AppointmentEntity> findAllByFilter(AppointmentFilterRequest filter) {
        Specification<AppointmentEntity> filters = specification.filter(filter);
        return repository.findAll(filters);
    }

    public List<AppointmentEntity> findByScheduleId(UUID scheduleId) {
        return repository.findByScheduleId(scheduleId);
    }


//    public AppointmentEntity activate (UUID scheduleId, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
//        AppointmentEntity entity = findById(scheduleId);
//
//        entity = applyPatch(patch, entity);
//
//        return repository.save(entity);
//    }
//

//
//    private AppointmentEntity applyPatch(JsonPatch patch, AppointmentEntity entity) throws JsonPatchException, JsonProcessingException {
//        JsonNode patched = patch.apply(objectMapper.convertValue(entity, JsonNode.class));
//        return objectMapper.treeToValue(patched, AppointmentEntity.class);
//    }
//

//
//    public List<AppointmentEntity> findAllByProductId(UUID productId) {
//        return repository.findAllByProductId(productId);
//    }
}
