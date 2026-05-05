package org.example.authservice.service.abstraction;

import org.example.authservice.dto.RoleAssignmentRequest;
import org.example.authservice.dto.ServiceResponse;
import org.example.authservice.dto.UserRequest;

public interface AuthService {
    ServiceResponse validateApiKey(String apiKey);

    ServiceResponse getUserByUsername(String username);

    ServiceResponse getUserByApiKey(String apiKey);

    ServiceResponse createUser(UserRequest request);

    ServiceResponse assignRoles(Long userId, RoleAssignmentRequest request);
}