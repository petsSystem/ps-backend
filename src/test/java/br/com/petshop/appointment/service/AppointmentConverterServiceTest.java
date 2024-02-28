package br.com.petshop.appointment.service;

import br.com.petshop.appointment.model.dto.request.AppointmentCreateRequest;
import br.com.petshop.appointment.model.dto.request.AppointmentCreateRequestMock;
import br.com.petshop.appointment.model.dto.request.AppointmentStatusRequest;
import br.com.petshop.appointment.model.dto.request.AppointmentStatusRequestMock;
import br.com.petshop.appointment.model.dto.request.AppointmentUpdateRequest;
import br.com.petshop.appointment.model.dto.request.AppointmentUpdateRequestMock;
import br.com.petshop.appointment.model.dto.response.AppointmentResponse;
import br.com.petshop.appointment.model.dto.response.AppointmentResponseMock;
import br.com.petshop.appointment.model.entity.AppointmentEntity;
import br.com.petshop.appointment.model.entity.AppointmentEntityMock;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

class AppointmentConverterServiceTest {
    @Mock ModelMapper mapper;
    @Mock Configuration configuration;
    @InjectMocks AppointmentConverterService appointmentConverterService;
    AppointmentEntity entity;
    AppointmentCreateRequest createRequest;
    AppointmentUpdateRequest updateRequest;
    AppointmentStatusRequest statusRequest;
    AppointmentResponse response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        entity = AppointmentEntityMock.get();
        createRequest = AppointmentCreateRequestMock.get();
        updateRequest = AppointmentUpdateRequestMock.get();
        statusRequest = AppointmentStatusRequestMock.get();
        response = AppointmentResponseMock.get();
    }

    @Test
    void testCreateRequestIntoEntity() {
        when(mapper.getConfiguration()).thenReturn(configuration);
        when(mapper.map(any(), any())).thenReturn(entity);
        AppointmentEntity result = appointmentConverterService.createRequestIntoEntity(createRequest);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testUpdateRequestIntoEntity() {
        when(mapper.getConfiguration()).thenReturn(configuration);
        when(mapper.map(any(), any())).thenReturn(entity);
        AppointmentEntity result = appointmentConverterService.updateRequestIntoEntity(updateRequest);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testUpdateRequestIntoEntity2() {
        when(mapper.getConfiguration()).thenReturn(configuration);
        when(mapper.map(any(), any())).thenReturn(entity);
        AppointmentEntity result = appointmentConverterService.updateRequestIntoEntity(entity, entity);
        Assertions.assertEquals(entity, result);
    }

    @Test
    void testEntityIntoResponse() {
        when(mapper.map(any(), any())).thenReturn(response);
        AppointmentResponse result = appointmentConverterService.entityIntoResponse(entity);
        Assertions.assertEquals(response, result);
    }
}