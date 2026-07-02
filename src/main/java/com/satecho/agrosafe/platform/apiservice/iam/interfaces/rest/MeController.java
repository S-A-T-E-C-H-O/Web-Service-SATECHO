package com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.iam.application.commandservices.UserCommandService;
import com.satecho.agrosafe.platform.apiservice.iam.application.queryservices.UserQueryService;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.ChangePasswordCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.commands.UpdateProfileCommand;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.queries.GetUserByIdQuery;
import com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.resources.ChangePasswordResource;
import com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.resources.UpdateProfileResource;
import com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.resources.UserResource;
import com.satecho.agrosafe.platform.apiservice.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.security.SecurityContextUtil;
import com.satecho.agrosafe.platform.apiservice.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/me", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Me", description = "Authenticated user profile endpoint")
@PreAuthorize("isAuthenticated()")
public class MeController {

    private final UserQueryService userQueryService;
    private final UserCommandService userCommandService;

    public MeController(UserQueryService userQueryService, UserCommandService userCommandService) {
        this.userQueryService = userQueryService;
        this.userCommandService = userCommandService;
    }

    @GetMapping
    public ResponseEntity<UserResource> getMe() {
        Long userId = SecurityContextUtil.getCurrentUserId();
        return userQueryService.handle(new GetUserByIdQuery(userId))
                .map(UserResourceFromEntityAssembler::toResourceFromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping
    public ResponseEntity<?> updateProfile(@RequestBody UpdateProfileResource resource) {
        Long userId = SecurityContextUtil.getCurrentUserId();
        var command = new UpdateProfileCommand(userId, resource.fullName());
        var result = userCommandService.updateProfile(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, UserResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordResource resource) {
        Long userId = SecurityContextUtil.getCurrentUserId();
        var command = new ChangePasswordCommand(userId, resource.currentPassword(), resource.newPassword());
        var result = userCommandService.changePassword(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, v -> Map.of("message", "Password updated successfully"), HttpStatus.OK);
    }
}
