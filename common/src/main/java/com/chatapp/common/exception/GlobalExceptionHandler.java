package com.chatapp.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Global exception handler for consistent error responses across services.
 * This class can be extended by each microservice to add service-specific exception handling.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle ChatAppException and its subclasses.
     */
    @ExceptionHandler(ChatAppException.class)
    public ResponseEntity<ApiError> handleChatAppException(ChatAppException ex, HttpServletRequest request) {
        log.error("ChatAppException: {}", ex.getMessage(), ex);
        
        ApiError apiError = ApiError.builder()
                .status(HttpStatus.valueOf(ex.getStatusCode()).name())
                .statusCode(ex.getStatusCode())
                .message(ex.getMessage())
                .errorCode(ex.getErrorCode())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        
        return new ResponseEntity<>(apiError, HttpStatus.valueOf(ex.getStatusCode()));
    }

    /**
     * Handle validation exceptions from @Valid annotations.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        log.error("Validation error: {}", ex.getMessage());
        
        List<ApiError.ValidationError> validationErrors = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            ApiError.ValidationError error = ApiError.ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .rejectedValue(fieldError.getRejectedValue())
                    .build();
            validationErrors.add(error);
        }
        
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed")
                .errorCode("VALIDATION_FAILED")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .errors(validationErrors)
                .build();
    }

    /**
     * Handle constraint violation exceptions.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
        log.error("Constraint violation: {}", ex.getMessage());
        
        List<ApiError.ValidationError> validationErrors = new ArrayList<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            ApiError.ValidationError error = ApiError.ValidationError.builder()
                    .field(violation.getPropertyPath().toString())
                    .message(violation.getMessage())
                    .rejectedValue(violation.getInvalidValue())
                    .build();
            validationErrors.add(error);
        }
        
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Constraint violation")
                .errorCode("CONSTRAINT_VIOLATION")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .errors(validationErrors)
                .build();
    }

    /**
     * Handle bind exceptions.
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBindException(BindException ex, HttpServletRequest request) {
        log.error("Bind exception: {}", ex.getMessage());
        
        List<ApiError.ValidationError> validationErrors = new ArrayList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            ApiError.ValidationError error = ApiError.ValidationError.builder()
                    .field(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .rejectedValue(fieldError.getRejectedValue())
                    .build();
            validationErrors.add(error);
        }
        
        return ApiError.builder()
                .status(HttpStatus.BAD_REQUEST.name())
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .message("Bind exception")
                .errorCode("BIND_EXCEPTION")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .errors(validationErrors)
                .build();
    }

    /**
     * Handle all other exceptions.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleAllExceptions(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception: {}", ex.getMessage(), ex);
        
        return ApiError.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.name())
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("An unexpected error occurred")
                .errorCode("INTERNAL_SERVER_ERROR")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
    }
}