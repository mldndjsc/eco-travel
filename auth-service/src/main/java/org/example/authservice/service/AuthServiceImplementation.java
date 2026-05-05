package org.example.authservice.service;

import lombok.RequiredArgsConstructor;
import org.example.authservice.dto.RoleAssignmentRequest;
import org.example.authservice.dto.ServiceResponse;
import org.example.authservice.dto.UserAuthResponse;
import org.example.authservice.dto.UserRequest;
import org.example.authservice.dto.UserResponse;
import org.example.authservice.exception.BadRequestException;
import org.example.authservice.exception.NotFoundException;
import org.example.authservice.model.Role;
import org.example.authservice.model.User;
import org.example.authservice.repository.RoleRepository;
import org.example.authservice.repository.UserRepository;
import org.example.authservice.service.abstraction.AuthService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImplementation implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public ServiceResponse validateApiKey(String apiKey) {
        User user = userRepository.findByApiKey(apiKey)
                .orElseThrow(() -> new NotFoundException("User not found for provided API key"));
        return ServiceResponse.ok(new UserAuthResponse(user.getUsername(), roleNames(user)));
    }

    @Override
    public ServiceResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found with username: " + username));
        return ServiceResponse.ok(toResponse(user));
    }

    @Override
    public ServiceResponse getUserByApiKey(String apiKey) {
        User user = userRepository.findByApiKey(apiKey)
                .orElseThrow(() -> new NotFoundException("User not found for provided API key"));
        return ServiceResponse.ok(toResponse(user));
    }

    @Override
    public ServiceResponse createUser(UserRequest request) {
        if (userRepository.findByUsername(request.username()).isPresent())
            throw new BadRequestException("Username already exists: " + request.username());
        if (userRepository.findByApiKey(request.apiKey()).isPresent())
            throw new BadRequestException("API key already exists");

        List<Role> roles = resolveRoles(request.roleNames());

        User user = User.builder()
                .username(request.username())
                .apiKey(request.apiKey())
                .roles(roles)
                .build();

        User saved = userRepository.save(user);
        return ServiceResponse.created(toResponse(saved));
    }

    @Override
    public ServiceResponse assignRoles(Long userId, RoleAssignmentRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));

        List<Role> roles = resolveRoles(request.roleNames());
        user.setRoles(roles);
        User saved = userRepository.save(user);
        return ServiceResponse.ok(toResponse(saved));
    }

    private List<Role> resolveRoles(List<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty())
            return Collections.emptyList();

        return roleNames.stream()
                .map(name -> roleRepository.findByName(name)
                        .orElseThrow(() -> new NotFoundException("Role not found: " + name)))
                .toList();
    }

    private List<String> roleNames(User user) {
        return user.getRoles().stream().map(Role::getName).toList();
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getUsername(), user.getApiKey(), roleNames(user));
    }
}