package com.sentinel.audit.api.exception;

import com.sentinel.audit.application.dto.ErrorResponse;
import com.sentinel.audit.domain.exception.KycProviderException;
import com.sentinel.audit.domain.exception.RegulatoryDataException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidationException(
                        MethodArgumentNotValidException ex,
                        HttpServletRequest request) {

                log.error("Validation error on path: {}", request.getRequestURI(), ex);

                List<ErrorResponse.ValidationError> validationErrors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(error -> new ErrorResponse.ValidationError(
                                                error.getField(),
                                                error.getDefaultMessage()))
                                .collect(Collectors.toList());

                ErrorResponse errorResponse = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.BAD_REQUEST.value(),
                                "Validation Failed",
                                "Invalid request parameters",
                                request.getRequestURI(),
                                validationErrors);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        @ExceptionHandler(KycProviderException.class)
        public ResponseEntity<ErrorResponse> handleKycProviderException(
                        KycProviderException ex,
                        HttpServletRequest request) {

                log.error("KYC Provider error: {} (Provider: {}, Code: {})",
                                ex.getMessage(), ex.getProviderId(), ex.getErrorCode(), ex);

                ErrorResponse errorResponse = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.SERVICE_UNAVAILABLE.value(),
                                "KYC Provider Error",
                                String.format("KYC verification failed: %s (Code: %s)", ex.getMessage(),
                                                ex.getErrorCode()),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);
        }

        @ExceptionHandler(RegulatoryDataException.class)
        public ResponseEntity<ErrorResponse> handleRegulatoryDataException(
                        RegulatoryDataException ex,
                        HttpServletRequest request) {

                log.error("Regulatory data error from source: {}", ex.getSource(), ex);

                ErrorResponse errorResponse = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Regulatory Data Error",
                                String.format("Failed to retrieve regulatory data: %s", ex.getMessage()),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleGenericException(
                        Exception ex,
                        HttpServletRequest request) {

                log.error("Unexpected error on path: {}", request.getRequestURI(), ex);

                ErrorResponse errorResponse = new ErrorResponse(
                                LocalDateTime.now(),
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "Internal Server Error",
                                "An unexpected error occurred. Please contact support.",
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
}
