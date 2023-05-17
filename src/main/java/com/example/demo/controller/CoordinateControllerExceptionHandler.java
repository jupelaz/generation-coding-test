package com.example.demo.controller;

import com.example.demo.model.dto.GenericResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class CoordinateControllerExceptionHandler {
    @ExceptionHandler(IOException.class)
    public ResponseEntity<GenericResponse> handleIOException(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        GenericResponse
                                .builder()
                                .message("There was an internal error. Try it again later.")
                                .build()
                );

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult().getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
        return new ResponseEntity<>(
                Map.of("errors",errors),
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse> handleGenericException(Exception e) {
        return new ResponseEntity<>(GenericResponse.builder().message(e.getMessage()).build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
