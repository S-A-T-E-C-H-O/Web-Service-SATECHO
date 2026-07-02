package com.satecho.agrosafe.platform.apiservice.activitylog.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.activitylog.application.queryservices.ActivityLogQueryService;
import com.satecho.agrosafe.platform.apiservice.activitylog.domain.model.valueobjects.ActivityType;
import com.satecho.agrosafe.platform.apiservice.activitylog.interfaces.rest.resources.ActivityLogEntryResource;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.security.ResourceOwnershipService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** EP-004-US020: chronological log of irrigation, alert, security, and threshold-change events for a farm. */
@RestController
@RequestMapping(value = "/api/v1/activity-log", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Activity Log", description = "Per-farm chronological activity feed")
public class ActivityLogController {

    private final ActivityLogQueryService activityLogQueryService;
    private final ResourceOwnershipService resourceOwnershipService;

    public ActivityLogController(ActivityLogQueryService activityLogQueryService, ResourceOwnershipService resourceOwnershipService) {
        this.activityLogQueryService = activityLogQueryService;
        this.resourceOwnershipService = resourceOwnershipService;
    }

    @GetMapping
    public ResponseEntity<List<ActivityLogEntryResource>> getActivityLog(
            @RequestParam Long farmId,
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {
        if (!resourceOwnershipService.isFarmOwnerOrAdmin(farmId)) return ResponseEntity.status(403).build();

        ActivityType activityType = null;
        if (type != null && !type.isBlank()) {
            try {
                activityType = ActivityType.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().build();
            }
        }

        var entries = activityLogQueryService.findByFarm(farmId, activityType, page, size).stream()
                .map(e -> new ActivityLogEntryResource(e.getId(), e.getFarmId(), e.getType().name(),
                        e.getDescription(), e.getOccurredAt()))
                .toList();
        return ResponseEntity.ok(entries);
    }
}
