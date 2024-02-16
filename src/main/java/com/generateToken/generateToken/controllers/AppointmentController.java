package com.generateToken.generateToken.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.generateToken.generateToken.dto.AppointmentDTOs;
import com.generateToken.generateToken.entities.ApiResponse;
import com.generateToken.generateToken.entities.Appointment;
import com.generateToken.generateToken.exceptionsHandler.VerificationExceptionHandler;
import com.generateToken.generateToken.services.AppointmentService;
import com.generateToken.generateToken.util.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/appointment")
@CrossOrigin("http://localhost:3000")
public class AppointmentController {

  @Autowired
  private AppointmentService appointmentService;

  private <T> ApiResponse<T> createApiResponse(HttpStatus status, String message, T data) {
    ApiResponse<T> response = new ApiResponse<>();
    response.setStatusCode(status.value());
    response.setMessage(message);
    response.setData(data);
    return response;
  }

  @PostMapping("/book1")
  public ResponseEntity<?> bookAppointment(HttpServletRequest httpServletRequest,
      @RequestParam Long clinicId,
      @RequestBody AppointmentDTOs appointmentPatient) {
    AppointmentDTOs appointmentDTOs = new AppointmentDTOs();
    try {
      String token = extractTokenFromRequest(httpServletRequest);
      String email = JwtUtil.getEmailFromToken(token);
      appointmentDTOs = appointmentService.bookAppointment(email, clinicId, appointmentPatient);
    } catch (VerificationExceptionHandler e) {
      throw e;
    }
    if (appointmentDTOs == null) {
      return new ResponseEntity<>(createApiResponse(HttpStatus.NOT_FOUND, "No appointment booked", null),
          HttpStatus.NOT_FOUND);
    } else {
      return new ResponseEntity<>(createApiResponse(HttpStatus.OK, "Appointment booked", appointmentDTOs),
          HttpStatus.OK);
    }
  }

  @GetMapping("/getByContactNumber")
  public ResponseEntity<?> getPatientByContact(@RequestParam String contact) {
    List<Appointment> patient = new ArrayList<Appointment>();
    try {
      patient = appointmentService.getByContactNumber(contact);
    } catch (VerificationExceptionHandler e) {
      throw e;
    }

    if (patient != null) {
      return new ResponseEntity<>(
          createApiResponse(HttpStatus.OK, "All Appointments according to Contact number", patient), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(
          createApiResponse(HttpStatus.NOT_FOUND, "No Appointments According to Contact number", null),
          HttpStatus.NOT_FOUND);
    }
  }

  @GetMapping("/getByAbhaNumber")
  public ApiResponse<List<Appointment>> getPatientByAbhaNumber(@RequestParam String abhaNumber) {

    List<Appointment> patient = appointmentService.getByAbhaNumber(abhaNumber);

    if (patient.size() != 0) {
      return createApiResponse(HttpStatus.OK, "List Of Appointments Bases On Abha Number", patient);
    } else {
      return createApiResponse(HttpStatus.NOT_FOUND, "No List found Of Appointments Bases On Abha Number", null);
    }
  }

  private String extractTokenFromRequest(HttpServletRequest request) {
    // Try to extract token from Authorization header
    String authorizationHeader = request.getHeader("Authorization");
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
      return authorizationHeader.substring(7); // Skip "Bearer "
    }

    // If not found in Authorization header, try to extract from query parameter
    String tokenFromQueryParam = request.getParameter("token");
    if (tokenFromQueryParam != null) {
      return tokenFromQueryParam;
    }

    // If not found in query parameter, try to extract from cookies
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if ("token".equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }

    // If not found in cookies, try to extract from request body (assuming it's a
    // POST request)
    // This part depends on your application's specific request structure
    // For simplicity, we'll assume a form parameter named "token"
    String tokenFromBody = request.getParameter("token");
    if (tokenFromBody != null) {
      return tokenFromBody;
    }

    // If token is not found in any location, return null
    return null;
  }

}
