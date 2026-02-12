package com.openclassrooms.mddapi.dto.response;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standard error response structure for API errors.
 *
 * @param status    HTTP status code
 * @param message   error message
 * @param errors    map of field-specific errors
 * @param timestamp when the error occurred
 */
public record ErrorResponse(
    int status,
    String message,
    Map<String, String> errors,
    LocalDateTime timestamp
) {

}