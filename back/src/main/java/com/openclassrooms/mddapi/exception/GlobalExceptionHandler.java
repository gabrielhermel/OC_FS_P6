package com.openclassrooms.mddapi.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for REST API. Provides consistent error responses across all endpoints.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Handles validation errors from @Valid annotations.
   *
   * @param ex validation exception
   * @return error response with field-specific messages
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    ErrorResponse response = new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        "Validation failed",
        errors,
        LocalDateTime.now()
    );

    return ResponseEntity.badRequest().body(response);
  }

  /**
   * Handles authentication failures (bad credentials).
   *
   * @param ex authentication exception
   * @return error response
   */
  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
    ErrorResponse response = new ErrorResponse(
        HttpStatus.UNAUTHORIZED.value(),
        "Invalid username or password",
        null,
        LocalDateTime.now()
    );

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
  }

  /**
   * Handles user not found errors during authentication. Returns 401 to avoid revealing whether
   * username exists.
   *
   * @param ex user not found exception
   * @return error response
   */
  @ExceptionHandler(UsernameNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFound(UsernameNotFoundException ex) {
    ErrorResponse response = new ErrorResponse(
        HttpStatus.UNAUTHORIZED.value(),
        "Invalid username or password", // Generic message
        null,
        LocalDateTime.now()
    );

    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
  }

  /**
   * Handles all other unexpected exceptions.
   *
   * @param ex generic exception
   * @return error response
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
    ErrorResponse response = new ErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "An unexpected error occurred",
        null,
        LocalDateTime.now()
    );

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }
}