package com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.iam.application.queryservices.RoleQueryService;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.queries.GetAllRolesQuery;
import com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.resources.RoleResource;
import com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.transform.RoleResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for role queries.
 */
@RestController
@RequestMapping(value = "/api/v1/roles", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Roles", description = "Role management endpoints")
public class RolesController {

    private final RoleQueryService roleQueryService;

    public RolesController(RoleQueryService roleQueryService) {
        this.roleQueryService = roleQueryService;
    }

    /**
     * Get all roles.
     * GET /api/v1/roles
     */
    @GetMapping
    public ResponseEntity<List<RoleResource>> getAllRoles() {
        var getAllRolesQuery = new GetAllRolesQuery();
        var roles = roleQueryService.handle(getAllRolesQuery);
        var roleResources = roles.stream()
                .map(RoleResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(roleResources);
    }
}