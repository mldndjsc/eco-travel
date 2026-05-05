package org.example.gatewayservice.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class ErrorResponseDto {
    private LocalDateTime timestamp;
    private String message;
    private String traceId;
    private int status;
    private String path;
    private Map<String, ?> details;

    public ErrorResponseDto(String message, String traceId, int status) {
        this(message, traceId, status, null, null);
    }

    public ErrorResponseDto(String message, String traceId, int status, String path, Map<String, ?> details) {
        this.timestamp = LocalDateTime.now();
        this.message = message;
        this.traceId = traceId;
        this.status = status;
        this.path = path;
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, ?> getDetails() {
        return details;
    }

    public void setDetails(Map<String, ?> details) {
        this.details = details;
    }
}