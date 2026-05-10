package org.example.authservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.authservice.dto.RoleAssignmentRequest;
import org.example.authservice.dto.ServiceResponse;
import org.example.authservice.dto.UserRequest;
import org.example.authservice.service.AuthServiceImplementation;
import org.example.authservice.service.abstraction.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Operations for validating users and roles")
public class AuthController {

    private final AuthServiceImplementation authService;

    @GetMapping("/validate")
    @Operation(summary = "Validate API key and return user roles")
    public ResponseEntity<Object> validate(
            @Parameter(description = "API key") @RequestHeader("X-API-KEY") String apiKey) {
        ServiceResponse response = authService.validateApiKey(apiKey);
        return ResponseEntity.status(response.statusCode()).body(response.data());
    }

    @GetMapping("/users/by-username/{username}")
    @Operation(summary = "Get user by username")
    public ResponseEntity<Object> getByUsername(
            @Parameter(description = "Username") @PathVariable String username) {
        ServiceResponse response = authService.getUserByUsername(username);
        return ResponseEntity.status(response.statusCode()).body(response.data());
    }

    @GetMapping("/users/by-api-key")
    @Operation(summary = "Get user by API key")
    public ResponseEntity<Object> getByApiKey(
            @Parameter(description = "API key") @RequestParam String apiKey) {
        ServiceResponse response = authService.getUserByApiKey(apiKey);
        return ResponseEntity.status(response.statusCode()).body(response.data());
    }

    @PostMapping("/users")
    @Operation(summary = "Create a user with optional roles")
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserRequest request) {
        ServiceResponse response = authService.createUser(request);
        return ResponseEntity.status(response.statusCode()).body(response.data());
    }

    @PutMapping("/users/{id}/roles")
    @Operation(summary = "Assign roles to a user")
    public ResponseEntity<Object> assignRoles(
            @Parameter(description = "User ID") @PathVariable Long id,
            @Valid @RequestBody RoleAssignmentRequest request) {
        ServiceResponse response = authService.assignRoles(id, request);
        return ResponseEntity.status(response.statusCode()).body(response.data());
    }
}