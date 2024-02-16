package com.generateToken.generateToken.controllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.generateToken.generateToken.controllers.ClinicController;
import com.generateToken.generateToken.dto.AppointmentDTOs;
import com.generateToken.generateToken.dto.ClinicDto;
import com.generateToken.generateToken.entities.Clinic;
import com.generateToken.generateToken.services.AppointmentService;
import com.generateToken.generateToken.services.ClinicService;

class ClinicControllerTest {

    @Mock
    private ClinicService clinicService;

    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    private ClinicController clinicController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // @Test
    // void addClinicsToDoctor() {
    // // Arrange
    // Long userId = 1L;
    // ClinicDto clinicDto = new ClinicDto();
    // when(clinicService.addClinic(anyLong(),
    // any(ClinicDto.class))).thenReturn(clinicDto);
    //
    // // Act
    // ResponseEntity<?> responseEntity =
    // clinicController.addClinicsToDoctor(userId, clinicDto);
    //
    // // Assert
    // assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    // assertEquals(clinicDto, responseEntity.getBody());
    // verify(clinicService, times(1)).addClinic(anyLong(), any(ClinicDto.class));
    // }

    // @Test
    // void getClinicById() {
    // // Arrange
    // Long clinicId = 1L;
    // Clinic clinic = new Clinic();
    // when(clinicService.getClinicById(anyLong())).thenReturn(Optional.of(clinic));
    //
    // // Act
    // ResponseEntity<Clinic> responseEntity =
    // clinicController.getClinicById(clinicId);
    //
    // // Assert
    // assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    // assertEquals(clinic, responseEntity.getBody());
    // verify(clinicService, times(1)).getClinicById(anyLong());
    // }

    @Test
    void deleteClinic() {
        // Arrange
        Long doctorId = 1L;
        Long clinicId = 2L;
        when(clinicService.deleteClinic(anyLong(), anyLong())).thenReturn("Deleted");

        // Act
        ResponseEntity<?> responseEntity = clinicController.deleteClinic(doctorId, clinicId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Deleted", responseEntity.getBody());
        verify(clinicService, times(1)).deleteClinic(anyLong(), anyLong());
    }

    @Test
    void updateClinic() {
        // Arrange
        Long clinicId = 1L;
        ClinicDto updatedClinicDto = new ClinicDto();
        when(clinicService.updateClinic(anyLong(), any(ClinicDto.class))).thenReturn(updatedClinicDto);

        // Act
        ResponseEntity<Object> responseEntity = clinicController.updateClinic(clinicId, updatedClinicDto);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedClinicDto, responseEntity.getBody());
        verify(clinicService, times(1)).updateClinic(anyLong(), any(ClinicDto.class));
    }

    @Test
    void getAppointments() {
        // Arrange
        Long clinicId = 1L;
        List<AppointmentDTOs> appointmentDTOsList = new ArrayList<>();
        when(clinicService.getAppointments(anyLong())).thenReturn(appointmentDTOsList);

        // Act
        ResponseEntity<?> responseEntity = clinicController.getAppointments(clinicId);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(appointmentDTOsList, responseEntity.getBody());
        verify(clinicService, times(1)).getAppointments(anyLong());
    }

    @Test
    void getAllClinics() {
        // Arrange
        List<Clinic> clinicList = new ArrayList<>();
        when(clinicService.getAllClinics()).thenReturn(clinicList);

        // Act
        List<Clinic> result = clinicController.getAllClinics();

        // Assert
        assertEquals(clinicList, result);
        verify(clinicService, times(1)).getAllClinics();
    }

    @Test
    void findAmountForClinicInDateRange() {
        // Arrange
        Long clinicId = 1L;
        Date startDate = new Date();
        Date endDate = new Date();
        Double amount = 100.0;
        when(clinicService.findAmountForClinicInDateRange(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(amount);

        // Act
        ResponseEntity<Double> responseEntity = clinicController.findAmountForClinicInDateRange(clinicId, startDate,
                endDate);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(amount, responseEntity.getBody());
        verify(clinicService, times(1)).findAmountForClinicInDateRange(anyLong(), any(Date.class), any(Date.class));
    }
}
