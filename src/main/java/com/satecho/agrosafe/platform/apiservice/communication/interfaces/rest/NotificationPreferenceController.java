package com.satecho.agrosafe.platform.apiservice.communication.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.communication.application.commandservices.PreferenceCommandService;
import com.satecho.agrosafe.platform.apiservice.communication.application.querydservices.PreferenceQueryService;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.queries.GetNotificationPreferencesByUserIdQuery;
import com.satecho.agrosafe.platform.apiservice.communication.interfaces.rest.resources.NotificationPreferenceResource;
import com.satecho.agrosafe.platform.apiservice.communication.interfaces.rest.resources.UpdatePreferencesResource;
import com.satecho.agrosafe.platform.apiservice.communication.interfaces.rest.transform.NotificationPreferenceResourceFromEntityAssembler;
import com.satecho.agrosafe.platform.apiservice.communication.interfaces.rest.transform.UpdateNotificationPreferencesCommandFromResourceAssembler;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.security.SecurityContextUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/notifications/preferences", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Notification Preferences", description = "Preference management endpoints")
public class NotificationPreferenceController {
    private final PreferenceCommandService preferenceCommandService;
    private final PreferenceQueryService preferenceQueryService;

    public NotificationPreferenceController(PreferenceCommandService preferenceCommandService, PreferenceQueryService preferenceQueryService) {
        this.preferenceCommandService = preferenceCommandService;
        this.preferenceQueryService = preferenceQueryService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationPreferenceResource>> getPreferences() {
        Long userId = SecurityContextUtil.getCurrentUserId();
        return ResponseEntity.ok(NotificationPreferenceResourceFromEntityAssembler.toResourceList(
                preferenceQueryService.handle(new GetNotificationPreferencesByUserIdQuery(userId))));
    }

    @PutMapping
    public ResponseEntity<?> updatePreferences(@RequestBody UpdatePreferencesResource resource) {
        Long userId = SecurityContextUtil.getCurrentUserId();
        var result = preferenceCommandService.updatePreferences(
                UpdateNotificationPreferencesCommandFromResourceAssembler.toCommand(userId, resource));
        if (result.isSuccess()) return ResponseEntity.ok(NotificationPreferenceResourceFromEntityAssembler.toResource(result.toOptional().orElseThrow()));
        return ResponseEntity.badRequest().build();
    }
}