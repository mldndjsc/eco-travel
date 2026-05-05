package org.example.authservice.dto;

public record ServiceResponse(
        int statusCode,
        Object data
) {
    public static ServiceResponse ok(Object data) {
        return new ServiceResponse(200, data);
    }

    public static ServiceResponse created(Object data) {
        return new ServiceResponse(201, data);
    }
}