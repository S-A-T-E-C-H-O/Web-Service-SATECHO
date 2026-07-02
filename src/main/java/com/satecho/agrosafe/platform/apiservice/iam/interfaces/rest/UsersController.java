package com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.iam.application.commandservices.UserCommandService;
import com.satecho.agrosafe.platform.apiservice.iam.application.queryservices.UserQueryService;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.queries.GetAllUsersQuery;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.queries.GetUserByIdQuery;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.valueobjects.Roles;
import com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.resources.UserResource;
import com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import com.satecho.agrosafe.platform.apiservice.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for user management (admin operations, EP-011-US001).
 */
@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "User management endpoints")
@PreAuthorize("hasRole('ADMIN')")
public class UsersController {

    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    public UsersController(UserQueryService userQueryService, UserCommandService userCommandService) {
        this.userQueryService = userQueryService;
        this.userCommandService = userCommandService;
    }

    /**
     * Get all users, optionally filtered by role and/or blocked status.
     * GET /api/v1/users?role=AGRONOMIST&blocked=false
     */
    @GetMapping
    public ResponseEntity<List<UserResource>> getAllUsers(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Boolean blocked) {
        var users = userQueryService.handle(new GetAllUsersQuery());
        var stream = users.stream();
        if (role != null && !role.isBlank()) {
            Roles roleEnum;
            try {
                roleEnum = Roles.valueOf(role.startsWith("ROLE_") ? role : "ROLE_" + role.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
            stream = stream.filter(u -> u.getRoles().stream().anyMatch(r -> r.getName() == roleEnum));
        }
        if (blocked != null) {
            stream = stream.filter(u -> blocked.equals(u.getBlocked() != null ? u.getBlocked() : false));
        }
        var userResources = stream.map(UserResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(userResources);
    }

    /**
     * Get user by id.
     * GET /api/v1/users/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResource> getUserById(@PathVariable Long userId) {
        var getUserByIdQuery = new GetUserByIdQuery(userId);
        var user = userQueryService.handle(getUserByIdQuery);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(user.get());
        return ResponseEntity.ok(userResource);
    }

    /**
     * Blocks a user, preventing them from signing in.
     * POST /api/v1/users/{userId}/block
     */
    @PostMapping("/{userId}/block")
    public ResponseEntity<?> blockUser(@PathVariable Long userId) {
        var result = userCommandService.setBlocked(userId, true);
        return ResponseEntityAssembler.toResponseEntityFromResult(result, UserResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }

    /**
     * Unblocks a previously blocked user.
     * POST /api/v1/users/{userId}/unblock
     */
    @PostMapping("/{userId}/unblock")
    public ResponseEntity<?> unblockUser(@PathVariable Long userId) {
        var result = userCommandService.setBlocked(userId, false);
        return ResponseEntityAssembler.toResponseEntityFromResult(result, UserResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }
}
