package com.generateToken.generateToken.controllers;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import com.generateToken.generateToken.dto.DoctorDTO;
import com.generateToken.generateToken.dto.SignupRequest;
import com.generateToken.generateToken.entities.ApiResponse;
import com.generateToken.generateToken.entities.Doctor;
import com.generateToken.generateToken.exceptionsHandler.VerificationExceptionHandler;
import com.generateToken.generateToken.repositories.DoctorRepository;
import com.generateToken.generateToken.services.DoctorService;
import com.generateToken.generateToken.util.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/home")
@CrossOrigin("http://localhost:3000")
// @RequestMapping(method = RequestMethod.POST, consumes =
// MediaType.APPLICATION_JSON_VALUE)
public class SignUpUserController {

  @Autowired
  private DoctorService doctorService;

  @Autowired
  private DoctorRepository doctorRepository;

  public static String uploadDir = System.getProperty("user.dir") + "src/main/webapp/images";

   private <T> ApiResponse<T> createApiResponse(HttpStatus status, String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatusCode(status.value());
        response.setMessage(message);
        response.setData(data);
        return response;
    }

  @PostMapping("/register")
  public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest) {
    DoctorDTO createdUser = doctorService.createUser(signupRequest);
    try{
        if (createdUser == null) {
      return new ResponseEntity<>(createApiResponse(HttpStatus.BAD_REQUEST, "Invalid signup request", null), HttpStatus.BAD_REQUEST);
    }else{
      return new ResponseEntity<>(createApiResponse(HttpStatus.CREATED, "User created successfully", createdUser), HttpStatus.CREATED);
    }
    }catch(VerificationExceptionHandler e){
      throw e;
  }
}


  // public byte[] getDoctorImage(Long doctorId) {
  // Optional<Doctor> optionalDoctor = doctorRepository.findById(doctorId);
  // if (optionalDoctor.isPresent()) {
  // Doctor doctor = optionalDoctor.get();
  // return doctor.getProfileImage();
  // }
  // return null; // Or handle the case when the doctor or image is not found
  // }

  // @GetMapping("/get")
  // public ResponseEntity<DoctorDTO> getUser(@RequestParam Long docId){
  // //String token = extractTokenFromRequest(request);
  // DoctorDTO doctor = doctorService.getDoctor(docId);
  //
  // if (doctor != null) {
  // return ResponseEntity.ok(doctor);
  // } else {
  // return ResponseEntity.notFound().build();
  // }
  // }

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

  @GetMapping("/get")
  public ResponseEntity<DoctorDTO> getUser(HttpServletRequest request) {
    String token = extractTokenFromRequest(request);
    if (token != null) {
      String email = JwtUtil.getEmailFromToken(token);
      System.out.println("id is" + " " + email);
      if (email != null) {
        DoctorDTO doctor = doctorService.getDoctor(email);
        return new ResponseEntity<>(doctor, HttpStatus.OK);
      }
    }
    return null;
  }

  @GetMapping("/getAllDoctors")
  public List<Doctor> getAllDoctors() {
    return doctorService.getAllDoctors();
  }

  @GetMapping("/amount")
  public ResponseEntity<?> getAmount(HttpServletRequest httpServletRequest,
      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
    String token = extractTokenFromRequest(httpServletRequest);
    String email = JwtUtil.getEmailFromToken(token);

    Double amt = doctorService.findAmt(email, startDate, endDate);
    return ResponseEntity.ok(amt);
  }

  @PutMapping("/updateDoctor")
  public Doctor updateDoctor(HttpServletRequest httpServletRequest, @RequestBody Doctor Doctor) {
    String token = extractTokenFromRequest(httpServletRequest);
    String email = JwtUtil.getEmailFromToken(token);
    return doctorService.updateDoctor(email, Doctor);
  }

  @PatchMapping("/up")
  public Doctor updateByField(HttpServletRequest httpServletRequest, @RequestBody Map<String, Object> fields) {
    String token = extractTokenFromRequest(httpServletRequest);
    String email = JwtUtil.getEmailFromToken(token);
    return doctorService.updateByFields(email, fields);
  }

  @DeleteMapping("/deleteDoctor")
  public String deleteDoctor(HttpServletRequest httpServletRequest) {
    String token = extractTokenFromRequest(httpServletRequest);
    String email = JwtUtil.getEmailFromToken(token);
    return doctorService.deleteDoctor(email);
  }

  @GetMapping("/payment")
  public RedirectView payment() {
    String paymentUrl = "http://localhost:9090/";
    return new RedirectView(paymentUrl);
  }

  @PutMapping("/forgotPassword")
  public ResponseEntity<ApiResponse> forgotPassword(@RequestParam String email, @RequestParam String newPassword,
      @RequestParam String confirmPassword) {
    ApiResponse apiResponse = doctorService.forgotPassword(email, newPassword, confirmPassword);
    if (apiResponse.getStatusCode() == 200) {
      return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    } else if (apiResponse.getStatusCode() == 501) {
      return new ResponseEntity<>(apiResponse, HttpStatus.NOT_IMPLEMENTED);
    } else if (apiResponse.getStatusCode() == 405) {
      return new ResponseEntity<>(apiResponse, HttpStatus.METHOD_NOT_ALLOWED);
    } else if (apiResponse.getStatusCode() == 411) {
      return new ResponseEntity<>(apiResponse, HttpStatus.LENGTH_REQUIRED);
    } else if (apiResponse.getStatusCode() == 400) {
      return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    } else {
      return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
