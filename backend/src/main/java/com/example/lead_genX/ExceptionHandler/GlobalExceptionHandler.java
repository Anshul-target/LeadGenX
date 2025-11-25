package com.example.lead_genX.ExceptionHandler;


import CustomException.BusinessException;
import CustomException.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MissingServletRequestParameterException;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

//    Validation Error

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(v ->
                errors.put(v.getPropertyPath().toString(), v.getMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // 2. CUSTOM BUSINESS EXCEPTIONS (CREATE YOUR OWN EXCEPTIONS)

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<?> handleBusinessException(BusinessException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    // -------------------------------------------------------------------------
    // 3. AUTHENTICATION & AUTHORIZATION
    // -------------------------------------------------------------------------
//    @ExceptionHandler(org.springframework.security.core.AuthenticationException.class)
//    public ResponseEntity<?> handleAuthError(Exception ex) {
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                .body("Authentication failed: " + ex.getMessage());
//    }
//
//    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
//    public ResponseEntity<?> handleAccessDenied(Exception ex) {
//        return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                .body("You are not allowed to perform this action.");
//    }

    // -------------------------------------------------------------------------
    // 4. RESOURCE NOT FOUND
    // -------------------------------------------------------------------------


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleCustomNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // 5. BAD REQUEST / INVALID INPUT / MALFORMED JSON

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }






    // 6. GENERIC / UNEXPECTED ERRORS (CATCH-ALL)

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception ex) {
        ex.printStackTrace(); // for debugging; remove in production
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred. Please try again later.");
    }
}

