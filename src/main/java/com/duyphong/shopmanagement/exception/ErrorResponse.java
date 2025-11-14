package com.duyphong.shopmanagement.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Standard error response structure for API errors
 */
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private LocalDateTime timestamp;

    private int status;

    private String error;

    private String message;

    private String path;

    /**
     * Field-specific validation errors
     * Key: field name, Value: error message
     */
    private Map<String, String> fieldErrors;
}