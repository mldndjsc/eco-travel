package org.example.gatewayservice.exceptions;

import org.example.gatewayservice.dto.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleAllExceptions(Exception ex, ServerWebExchange exchange) {
        HttpStatus status = ex instanceof ResponseStatusException responseStatusException
                ? HttpStatus.valueOf(responseStatusException.getStatusCode().value())
                : HttpStatus.INTERNAL_SERVER_ERROR;

        String traceId = exchange.getRequest().getHeaders().getFirst("X-Trace-Id");
        if (traceId == null || traceId.isBlank()) {
            traceId = "N/A";
        }

        String message = ex instanceof ResponseStatusException responseStatusException && responseStatusException.getReason() != null
                ? responseStatusException.getReason()
                : ex.getMessage();

        ErrorResponseDto error = new ErrorResponseDto(
                message,
                traceId,
                status.value(),
                exchange.getRequest().getURI().getPath(),
                null
        );

        return new ResponseEntity<>(error, status);
    }
}