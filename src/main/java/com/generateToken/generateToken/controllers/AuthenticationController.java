package com.generateToken.generateToken.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.generateToken.generateToken.dto.AuthenticationRequest;
import com.generateToken.generateToken.dto.AuthenticationResponse;
import com.generateToken.generateToken.entities.ApiResponse;
import com.generateToken.generateToken.exceptionsHandler.VerificationExceptionHandler;
import com.generateToken.generateToken.services.jwt.UserDetailsServiceImpl;
import com.generateToken.generateToken.util.JwtUtil;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@CrossOrigin("http://localhost:3000")
public class AuthenticationController {

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private <T> ApiResponse<T> createApiResponse(HttpStatus status, String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatusCode(status.value());
        response.setMessage(message);
        response.setData(data);
        return response;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> createAuthenticationToken(
            @RequestBody AuthenticationRequest authenticationRequest,
            HttpServletResponse response)
            throws BadCredentialsException, DisabledException, UsernameNotFoundException, IOException,
            VerificationExceptionHandler {

        try {
            try {
                authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                                authenticationRequest.getPassword()));
            } catch (BadCredentialsException e) {
                throw new BadCredentialsException("Incorrect username or password!");
            } catch (DisabledException disabledException) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "User is not activated");
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(createApiResponse(HttpStatus.NOT_FOUND, "User is not activated", null));
            }
        } catch (VerificationExceptionHandler e) {
            throw e;
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwt);
        return ResponseEntity.ok(createApiResponse(HttpStatus.OK, "Authentication successful", authenticationResponse));
    }

}
