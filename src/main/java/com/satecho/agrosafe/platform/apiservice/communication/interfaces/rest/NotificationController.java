package com.satecho.agrosafe.platform.apiservice.communication.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.communication.application.commandservices.NotificationCommandService;
import com.satecho.agrosafe.platform.apiservice.communication.application.querydservices.NotificationQueryService;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.commands.SendNotificationCommand;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.queries.GetNotificationsByUserIdQuery;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.DeliveryStatus;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.NotificationChannel;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.NotificationType;
import com.satecho.agrosafe.platform.apiservice.communication.interfaces.rest.resources.DispatchNotificationResource;
import com.satecho.agrosafe.platform.apiservice.communication.interfaces.rest.resources.NotificationResource;
import com.satecho.agrosafe.platform.apiservice.communication.interfaces.rest.resources.UpdateNotificationResource;
import com.satecho.agrosafe.platform.apiservice.communication.interfaces.rest.transform.NotificationResourceFromEntityAssembler;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.security.SecurityContextUtil;
import com.satecho.agrosafe.platform.apiservice.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/notifications", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Notifications", description = "Notification endpoints")
public class NotificationController {
    private final NotificationCommandService notificationCommandService;
    private final NotificationQueryService notificationQueryService;

    public NotificationController(NotificationCommandService notificationCommandService, NotificationQueryService notificationQueryService) {
        this.notificationCommandService = notificationCommandService;
        this.notificationQueryService = notificationQueryService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationResource>> getNotifications(
            @RequestParam(defaultValue = "20") Integer limit,
            @RequestParam(required = false) Boolean read,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status) {
        Long userId = SecurityContextUtil.getCurrentUserId();
        DeliveryStatus deliveryStatus = null;
        if (status != null && !status.isBlank()) {
            try { deliveryStatus = DeliveryStatus.valueOf(status.toUpperCase()); }
            catch (IllegalArgumentException e) { return ResponseEntity.badRequest().build(); }
        }
        String typeParam = category != null ? category : type;
        NotificationType notificationType = null;
        if (typeParam != null && !typeParam.isBlank()) {
            try { notificationType = NotificationType.valueOf(typeParam.toUpperCase()); }
            catch (IllegalArgumentException e) { return ResponseEntity.badRequest().build(); }
        }
        return ResponseEntity.ok(NotificationResourceFromEntityAssembler.toResourceList(
                notificationQueryService.handle(new GetNotificationsByUserIdQuery(userId, limit, deliveryStatus, notificationType, read))));
    }

    @GetMapping("/{notificationId}")
    public ResponseEntity<NotificationResource> getNotificationById(@PathVariable Long notificationId) {
        SecurityContextUtil.getCurrentUserId();
        return notificationQueryService.getById(notificationId)
                .map(n -> ResponseEntity.ok(NotificationResourceFromEntityAssembler.toResource(n)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{notificationId}")
    public ResponseEntity<?> updateNotification(@PathVariable Long notificationId,
                                                @RequestBody UpdateNotificationResource resource) {
        SecurityContextUtil.getCurrentUserId();
        if (Boolean.TRUE.equals(resource.read())) {
            var result = notificationCommandService.markAsRead(notificationId);
            return ResponseEntityAssembler.toResponseEntityFromResult(result, NotificationResourceFromEntityAssembler::toResource, HttpStatus.OK);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/dispatch")
    public ResponseEntity<?> dispatchNotification(@RequestBody DispatchNotificationResource resource) {
        SecurityContextUtil.getCurrentUserId();
        try {
            var command = new SendNotificationCommand(resource.userId(), NotificationType.valueOf(resource.type().toUpperCase()),
                    resource.title(), resource.body(), NotificationChannel.valueOf(resource.channel().toUpperCase()),
                    resource.relatedEntityId(), resource.relatedEntityType(), resource.recipientAddress());
            var result = notificationCommandService.sendNotification(command);
            return ResponseEntityAssembler.toResponseEntityFromResult(result, NotificationResourceFromEntityAssembler::toResource, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) { return ResponseEntity.badRequest().build(); }
    }
}