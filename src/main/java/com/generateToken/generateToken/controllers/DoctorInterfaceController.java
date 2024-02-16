package com.generateToken.generateToken.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.generateToken.generateToken.dto.DoctorDTO;
import com.generateToken.generateToken.entities.ApiResponse;
import com.generateToken.generateToken.entities.DoctorInterface;
import com.generateToken.generateToken.exceptionsHandler.VerificationExceptionHandler;
import com.generateToken.generateToken.services.DoctorInterfaceService;
import com.generateToken.generateToken.services.DoctorService;
import com.generateToken.generateToken.util.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/inter")
@CrossOrigin("http://localhost:3000")
public class DoctorInterfaceController {

  @Autowired
  private DoctorService doctorService;

  @Autowired
  private DoctorInterfaceService doctorInterfaceService;

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

  private <T> ApiResponse<T> createApiResponse(HttpStatus status, String message, T data) {
    ApiResponse<T> response = new ApiResponse<>();
    response.setStatusCode(status.value());
    response.setMessage(message);
    response.setData(data);
    return response;
  }

  @PostMapping("/add")
  public ResponseEntity<?> addInterface(HttpServletRequest request, @RequestBody DoctorInterface doctorInterfaceDto) {
    String token = extractTokenFromRequest(request);

    if (token == null) {
      return new ResponseEntity<>("Token not provided", HttpStatus.UNAUTHORIZED);
    }

    String email = JwtUtil.getEmailFromToken(token);

    if (email == null) {
      return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
    }
    try {
      DoctorDTO doctor = doctorService.getDoctor(email);

      if (doctor == null) {
        return new ResponseEntity<>("Doctor not found", HttpStatus.NOT_FOUND);
      }

      DoctorInterface createdDoctorInterface = doctorInterfaceService.addInt(doctorInterfaceDto, doctor);

      if (createdDoctorInterface != null) {
        return new ResponseEntity<>(
            createApiResponse(HttpStatus.OK, "DoctorInterface added successfully", createdDoctorInterface),
            HttpStatus.OK);
      } else {
        return new ResponseEntity<>(createApiResponse(HttpStatus.NOT_FOUND, "No DoctorInterface Found", null),
            HttpStatus.NOT_FOUND);
      }
    } catch (VerificationExceptionHandler e) {
      return new ResponseEntity<>(
          createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create DoctorInterface", null),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/get")
  public ResponseEntity<?> getInterface(HttpServletRequest request, @RequestParam LocalDate currentDate) {
    String token = extractTokenFromRequest(request);

    if (token == null) {
      return new ResponseEntity<>("Token not provided", HttpStatus.UNAUTHORIZED);
    }

    String email = JwtUtil.getEmailFromToken(token);

    if (email == null) {
      return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
    }

    DoctorDTO doctor = doctorService.getDoctor(email);

    if (doctor == null) {
      return new ResponseEntity<>("Doctor not found", HttpStatus.NOT_FOUND);
    }

    try {
      List<DoctorInterface> createdDoctorInterface = doctorInterfaceService.getInt(currentDate, doctor);

      if (createdDoctorInterface != null) {
        return new ResponseEntity<>(
            createApiResponse(HttpStatus.OK, "DoctorInterface get successfully", createdDoctorInterface),
            HttpStatus.OK);
      } else {
        return new ResponseEntity<>(createApiResponse(HttpStatus.NOT_FOUND, "No DoctorInterface Found", null),
            HttpStatus.NOT_FOUND);
      }
    } catch (VerificationExceptionHandler e) {
      return new ResponseEntity<>(
          createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to create DoctorInterface", null),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PutMapping("/update")
  public ResponseEntity<?> updateInterface(HttpServletRequest request, @RequestBody DoctorInterface doctorInterfaceDto,
      @RequestParam LocalDate currentDate) {
    String token = extractTokenFromRequest(request);

    if (token == null) {
      return new ResponseEntity<>("Token not provided", HttpStatus.UNAUTHORIZED);
    }

    String email = JwtUtil.getEmailFromToken(token);

    if (email == null) {
      return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
    }

    DoctorDTO doctor = doctorService.getDoctor(email);

    if (doctor == null) {
      return new ResponseEntity<>("Doctor not found", HttpStatus.NOT_FOUND);
    }
    try {
      DoctorInterface createdDoctorInterface = doctorInterfaceService.updateInt(doctorInterfaceDto, currentDate,
          doctor);

      if (createdDoctorInterface != null) {
        return new ResponseEntity<>(
            createApiResponse(HttpStatus.OK, "DoctorInterface updated successfully", createdDoctorInterface),
            HttpStatus.OK);
      } else {
        return new ResponseEntity<>(
            createApiResponse(HttpStatus.NOT_FOUND, "Failed to update DoctorInterface because no data is found", null),
            HttpStatus.NOT_FOUND);
      }
    } catch (VerificationExceptionHandler e) {
      throw e;
    }
  }

  @DeleteMapping("/delete")
  public ResponseEntity<?> deleteInterface(HttpServletRequest request, @RequestParam LocalDate currentDate,
      @RequestParam String clinicName) {
    String token = extractTokenFromRequest(request);

    if (token == null) {
      return new ResponseEntity<>("Token not provided", HttpStatus.UNAUTHORIZED);
    }

    String email = JwtUtil.getEmailFromToken(token);

    if (email == null) {
      return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
    }

    DoctorDTO doctor = doctorService.getDoctor(email);

    if (doctor == null) {
      return new ResponseEntity<>("Doctor not found", HttpStatus.NOT_FOUND);
    }

    try {
      String createdDoctorInterface = doctorInterfaceService.deleteInt(currentDate, clinicName, doctor);

      return new ResponseEntity<>(
          createApiResponse(HttpStatus.OK, "Deleted successfully doctorInterface", createdDoctorInterface),
          HttpStatus.OK);
    } catch (VerificationExceptionHandler e) {
      throw e;
    }
  }
}