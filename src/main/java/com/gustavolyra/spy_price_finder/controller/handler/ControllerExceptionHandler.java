package com.gustavolyra.spy_price_finder.controller.handler;

import com.gustavolyra.spy_price_finder.dto.error.ErrorDto;
import com.gustavolyra.spy_price_finder.dto.error.FieldErrorDto;
import com.gustavolyra.spy_price_finder.dto.error.ValidationErrorDto;
import com.gustavolyra.spy_price_finder.service.exceptions.HttpConectionException;
import com.gustavolyra.spy_price_finder.service.exceptions.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDto> handleResourceNotFoundException(ResourceNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        var errorDto = new ErrorDto(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(errorDto);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUsernameNotFoundException(UsernameNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        var errorDto = new ErrorDto(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(errorDto);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        var validationError = new ValidationErrorDto(Instant.now(), status.value(), "Validation error", request.getRequestURI());
        ex.getBindingResult().getFieldErrors().forEach(error ->
                validationError.addFieldError(new FieldErrorDto(error.getField(), error.getDefaultMessage())));
        return ResponseEntity.status(status).body(validationError);
    }

    @ExceptionHandler(HttpConectionException.class)
    public ResponseEntity<ErrorDto> handleHttpConectionException(HttpConectionException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_GATEWAY;
        var errorDto = new ErrorDto(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(errorDto);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDto> handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        var errorDto = new ErrorDto(Instant.now(), status.value(), e.getMessage(), request.getRequestURI());
        return ResponseEntity.status(status).body(errorDto);
    }


}
