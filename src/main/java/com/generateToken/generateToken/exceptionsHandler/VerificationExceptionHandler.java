package com.generateToken.generateToken.exceptionsHandler;

import java.nio.file.attribute.UserPrincipalNotFoundException;

import javax.naming.AuthenticationException;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.MethodNotAllowedException;

import com.generateToken.generateToken.entities.ApiResponse;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.mail.MessagingException;

@RestControllerAdvice
public class VerificationExceptionHandler extends RuntimeException {

    private static final Logger log = LoggerFactory.getLogger(VerificationExceptionHandler.class);

    private ApiResponse createApiResponse(HttpStatus status, String message) {
        ApiResponse response = new ApiResponse();
        response.setStatusCode(status.value());
        response.setMessage(message);
        response.setData(null);
        return response;
    }

    @ExceptionHandler(InvalidFieldException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleInvalidFieldException(InvalidFieldException exception) {
        return createApiResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler({ RuntimeException.class, MessagingException.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse handleInternalServerError(Exception e) {
        return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }

    @ExceptionHandler(UserPrincipalNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse handleUserNotFoundException(UserPrincipalNotFoundException e) {
        return createApiResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiResponse handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        return createApiResponse(HttpStatus.METHOD_NOT_ALLOWED, exception.getMessage());
    }

    @ExceptionHandler(MethodNotAllowedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiResponse handleMethodNotSupportedException(MethodNotAllowedException exception) {
        return createApiResponse(HttpStatus.METHOD_NOT_ALLOWED, exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse handleGeneralException(Exception exception) {
        log.error("Unhandled exception occurred", exception);
        return createApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred.");
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse handleAuthenticationException(AuthenticationException exception) {
        return createApiResponse(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleConstraintViolationException(ConstraintViolationException exception) {
        return createApiResponse(HttpStatus.BAD_REQUEST, "Invalid request. Please check your input.");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        return createApiResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse handleGeneralException(NullPointerException exception) {
        return createApiResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(AlreadyException.class)
    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    public ApiResponse handleAlreadyBoundException(AlreadyException ex) {
        return createApiResponse(HttpStatus.ALREADY_REPORTED, ex.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse handleExpiredJwtException(ExpiredJwtException e) {
        return createApiResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
    }
    // Other exception handlers can be added for different exception types.
}
