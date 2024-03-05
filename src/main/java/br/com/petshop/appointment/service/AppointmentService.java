package br.com.petshop.appointment.service;

import br.com.petshop.appointment.model.dto.request.AppointmentFilterRequest;
import br.com.petshop.appointment.model.dto.request.AppointmentStatusRequest;
import br.com.petshop.appointment.model.entity.AppointmentEntity;
import br.com.petshop.appointment.model.enums.Status;
import br.com.petshop.appointment.repository.AppointmentRepository;
import br.com.petshop.appointment.repository.AppointmentSpecification;
import br.com.petshop.commons.exception.GenericNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Classe responsável pelos serviços de agendamentos
 */
@Service
public class AppointmentService {
    private Logger log = LoggerFactory.getLogger(AppointmentService.class);
    @Autowired private AppointmentRepository repository;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private AppointmentSpecification specification;

    /**
     * Método que cria um agendamento
     * @param entity - entidade de agendamento
     * @return - entidade de agendamento
     */
    public AppointmentEntity create(AppointmentEntity entity) {
        return repository.save(entity);
    }

    /**
     * Método que atualiza um agendamento
     * @param entity - entidade de agendamento
     * @return - entidade de agendamento
     */
    public AppointmentEntity updateById(AppointmentEntity entity) {
        return repository.save(entity);
    }

    /**
     * Método que recupera um agendamento dado seu id
     * @param appointmentId - id de cadastro de agendamento
     * @return - entidade de agendamento
     */
    public AppointmentEntity findById(UUID appointmentId) {
        return repository.findById(appointmentId)
                .orElseThrow(GenericNotFoundException::new);
    }

    /**
     * Método que seta um status no agendamento
     * @param entity - entidade de agendamento
     * @param request - dto que contém dados de atualização de status do agendamento
     * @return - entidade de agendamento
     */
    public AppointmentEntity setStatus(AppointmentEntity entity, AppointmentStatusRequest request) {
        entity.setStatus(request.getStatus());

        if (request.getStatus() == Status.CANCELLED_BY_CLIENT ||
                request.getStatus() == Status.CANCELLED_BY_PETSHOP) {
            entity.setComments(request.getComments());
            entity.setActive(false);
        }

        return repository.save(entity);
    }

    /**
     * Método que filtra agendamentos
     * @param filter -
     * @return - lista de entidades de agendamento
     */
    public List<AppointmentEntity> findAllByFilter(AppointmentFilterRequest filter) {
        Specification<AppointmentEntity> filters = specification.filter(filter);
        return repository.findAll(filters);
    }
}
