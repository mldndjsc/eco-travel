package org.example.authservice.dto;

import java.util.List;

public record UserAuthResponse(
        String username,
        List<String> roles
) {
}