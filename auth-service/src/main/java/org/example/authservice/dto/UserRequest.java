package org.example.authservice.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record UserRequest(
        @NotBlank String username,
        @NotBlank String apiKey,
        List<String> roleNames
) {
}
