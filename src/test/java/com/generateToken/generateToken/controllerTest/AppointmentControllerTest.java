package com.generateToken.generateToken.controllerTest;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.generateToken.generateToken.controllers.AppointmentController;
import com.generateToken.generateToken.services.AppointmentService;

class AppointmentControllerTest {

    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    private AppointmentController appointmentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // @Test
    // void bookAppointment() {
    // // Arrange
    // Long doctorId = 1L;
    // Long clinicId = 2L;
    // AppointmentDTOs appointmentDTOs = new AppointmentDTOs();
    // when(appointmentService.bookAppointment(anyLong(), anyLong(),
    // any(AppointmentDTOs.class))).thenReturn(appointmentDTOs);
    //
    // // Act
    // AppointmentDTOs result = appointmentController.bookAppointment(doctorId,
    // clinicId, appointmentDTOs);
    //
    // // Assert
    // assertEquals(appointmentDTOs, result);
    // verify(appointmentService, times(1)).bookAppointment(anyLong(), anyLong(),
    // any(AppointmentDTOs.class));
    // }

}
