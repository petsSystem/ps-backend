package br.com.petshop.appointment.service;

import br.com.petshop.appointment.model.dto.request.AppointmentFilterRequest;
import br.com.petshop.appointment.model.dto.request.AppointmentFilterRequestMock;
import br.com.petshop.appointment.model.dto.request.AppointmentStatusRequest;
import br.com.petshop.appointment.model.dto.request.AppointmentStatusRequestMock;
import br.com.petshop.appointment.model.entity.AppointmentEntity;
import br.com.petshop.appointment.model.entity.AppointmentEntityMock;
import br.com.petshop.appointment.model.enums.Status;
import br.com.petshop.appointment.repository.AppointmentRepository;
import br.com.petshop.appointment.repository.AppointmentSpecification;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

class AppointmentServiceTest {
    @Mock AppointmentRepository repository;
    @Mock Specification<AppointmentEntity> specification;
    @Mock ObjectMapper objectMapper;
    @Mock AppointmentSpecification appointmentSpecification;
    @InjectMocks AppointmentService appointmentService;
    AppointmentEntity entity;
    AppointmentStatusRequest statusRequest;
    AppointmentFilterRequest filter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        entity = AppointmentEntityMock.get();
        filter = AppointmentFilterRequestMock.get();
        statusRequest = AppointmentStatusRequestMock.get();
    }

    @Test
    void testCreate() {
        when(repository.save(entity)).thenReturn(entity);
        AppointmentEntity result = appointmentService.create(entity);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testUpdateById() {
        when(repository.save(entity)).thenReturn(entity);
        AppointmentEntity result = appointmentService.updateById(entity);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testFindById() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        AppointmentEntity result = appointmentService.findById(id);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testSetStatus() {
        when(repository.save(entity)).thenReturn(entity);
        statusRequest.setStatus(Status.CANCELLED_BY_CLIENT);
        AppointmentEntity result = appointmentService.setStatus(entity, statusRequest);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testFindAllByFilter() {
        List<AppointmentEntity> entities = List.of(entity);
        when(repository.findAll(specification)).thenReturn(entities);

        List<AppointmentEntity> result = appointmentService.findAllByFilter(filter);
        Assertions.assertInstanceOf(List.class, result);
    }
}
