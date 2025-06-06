package com.example.backend.exceptions;

import org.hibernate.StaleObjectStateException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.net.ConnectException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException() {
        return new ResponseEntity<>("Invalid input, please try again", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleHttpMessageNotReadableException() {
        return new ResponseEntity<>("Invalid input, please try again", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxUploadSizeExceededException() {
        return new ResponseEntity<>("File is too large", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ OptimisticLockingFailureException.class, StaleObjectStateException.class })
    public ResponseEntity<String> handleOptimisticLocking(Exception e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Entity was modified or deleted by another transaction.");
    }

    @ExceptionHandler({DataAccessResourceFailureException.class, CannotGetJdbcConnectionException.class, ConnectException.class})
    public ResponseEntity<String> handleDataAccessResourceFailureException(Exception e) {
        return new ResponseEntity<>("Database connection is lost", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
