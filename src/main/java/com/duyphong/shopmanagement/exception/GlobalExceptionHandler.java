package com.duyphong.shopmanagement.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application
 * Handles validation errors and other common exceptions
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle validation errors from @Valid annotations
     * @param ex the MethodArgumentNotValidException
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        log.warn("Validation error occurred: {}", ex.getMessage());

        BindingResult bindingResult = ex.getBindingResult();
        Map<String, String> fieldErrors = new HashMap<>();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            fieldErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message("Request validation failed")
                .fieldErrors(fieldErrors)
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handle constraint validation errors from @NotEmpty, @NotNull etc. on path variables
     * @param ex the ConstraintViolationException
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(ConstraintViolationException ex) {
        log.warn("Constraint violation error occurred: {}", ex.getMessage());

        Map<String, String> fieldErrors = new HashMap<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            fieldErrors.put(fieldName, errorMessage);
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message("Request validation failed")
                .fieldErrors(fieldErrors)
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handle path variable type mismatch errors
     * @param ex the MethodArgumentTypeMismatchException
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        log.warn("Type mismatch error occurred: {}", ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Type Mismatch")
                .message(String.format("Invalid value '%s' for parameter '%s'", ex.getValue(), ex.getName()))
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handle JSON parsing errors (including date format and enum errors)
     * @param ex the HttpMessageNotReadableException
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonParseError(HttpMessageNotReadableException ex) {
        log.warn("JSON parsing error occurred: {}", ex.getMessage());

        String message = "Invalid JSON format";

        // Check if the error message contains MealType validation error
        if (ex.getMessage() != null && ex.getMessage().contains("MealType")) {
            Map<String, String> fieldErrors = new HashMap<>();
            fieldErrors.put("mealType", "Meal type must be either LUNCH or DINNER");

            ErrorResponse errorResponse = ErrorResponse.builder()
                    .timestamp(LocalDateTime.now())
                    .status(HttpStatus.BAD_REQUEST.value())
                    .error("Invalid Value")
                    .message("Invalid meal type value")
                    .fieldErrors(fieldErrors)
                    .build();

            return ResponseEntity.badRequest().body(errorResponse);
        }

        // Check if it's a format error (date or enum)
        Throwable cause = ex.getCause();
        if (cause instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatEx = (InvalidFormatException) cause;

            // Handle date format errors
            if (invalidFormatEx.getTargetType() != null &&
                    invalidFormatEx.getTargetType().getSimpleName().equals("LocalDate")) {

                String fieldName = "dueDate";
                if (invalidFormatEx.getPath() != null && !invalidFormatEx.getPath().isEmpty()) {
                    fieldName = invalidFormatEx.getPath().get(invalidFormatEx.getPath().size() - 1).getFieldName();
                }

                Map<String, String> fieldErrors = new HashMap<>();
                String dateErrorMessage = fieldName.equals("lunchDate") ?
                        "Lunch date format is invalid. Please use format: yyyy-MM-dd (e.g., 2025-09-17)" :
                        "Due date format is invalid. Please use format: yyyy-MM-dd (e.g., 2025-12-31)";
                fieldErrors.put(fieldName, dateErrorMessage);

                ErrorResponse errorResponse = ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error("Date Format Error")
                        .message("Invalid date format")
                        .fieldErrors(fieldErrors)
                        .build();

                return ResponseEntity.badRequest().body(errorResponse);
            }

            // Handle enum format errors (like MealType)
            if (invalidFormatEx.getTargetType() != null &&
                    invalidFormatEx.getTargetType().isEnum()) {

                String fieldName = "field";
                if (invalidFormatEx.getPath() != null && !invalidFormatEx.getPath().isEmpty()) {
                    fieldName = invalidFormatEx.getPath().get(invalidFormatEx.getPath().size() - 1).getFieldName();
                }

                Map<String, String> fieldErrors = new HashMap<>();
                String enumErrorMessage = fieldName.equals("mealType") ?
                        "Meal type must be either LUNCH or DINNER" :
                        String.format("Invalid value for %s. Please check allowed values.", fieldName);
                fieldErrors.put(fieldName, enumErrorMessage);

                ErrorResponse errorResponse = ErrorResponse.builder()
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .error("Invalid Value")
                        .message("Invalid enum value")
                        .fieldErrors(fieldErrors)
                        .build();

                return ResponseEntity.badRequest().body(errorResponse);
            }
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("JSON Parse Error")
                .message(message)
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handle generic exceptions
     * @param ex the Exception
     * @return ResponseEntity with error details
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error occurred", ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error("Internal Server Error")
                .message("An unexpected error occurred")
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
