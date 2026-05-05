package org.example.authservice.dto;

import java.util.List;

public record UserResponse(
        Long id,
        String username,
        String apiKey,
        List<String> roles
) {
}