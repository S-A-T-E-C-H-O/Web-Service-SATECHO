package com.satecho.agrosafe.platform.apiservice.communication.application.internal.eventhandlers;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.events.AlertCreatedEvent;
import com.satecho.agrosafe.platform.apiservice.communication.application.commandservices.NotificationCommandService;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.commands.SendNotificationCommand;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.NotificationChannel;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.NotificationType;
import com.satecho.agrosafe.platform.apiservice.communication.domain.repositories.DeviceTokenRepository;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.repositories.FarmRepository;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.repositories.ZoneRepository;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.events.IntrusionDetectedEvent;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.valueobjects.EventClassification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import static com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.AlertType.*;

/**
 * Bridges domain events raised in {@code advisory} (soil alerts) and {@code security} (PIR intrusion
 * detection) into push notifications, resolving the recipient farmer's userId and FCM tokens.
 */
@Component
public class AlertNotificationEventHandler {

    private static final Logger log = LoggerFactory.getLogger(AlertNotificationEventHandler.class);

    private final FarmRepository farmRepository;
    private final ZoneRepository zoneRepository;
    private final DeviceTokenRepository deviceTokenRepository;
    private final NotificationCommandService notificationCommandService;

    public AlertNotificationEventHandler(FarmRepository farmRepository, ZoneRepository zoneRepository,
                                         DeviceTokenRepository deviceTokenRepository,
                                         NotificationCommandService notificationCommandService) {
        this.farmRepository = farmRepository;
        this.zoneRepository = zoneRepository;
        this.deviceTokenRepository = deviceTokenRepository;
        this.notificationCommandService = notificationCommandService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onAlertCreated(AlertCreatedEvent event) {
        Long userId = resolveUserIdByFarmId(event.farmId());
        if (userId == null) {
            log.warn("Cannot dispatch push for alertId={}: farm owner not found (farmId={})", event.alertId(), event.farmId());
            return;
        }
        String zoneName = zoneRepository.findById(event.zoneId()).map(z -> z.getName()).orElse("your parcel");
        NotificationType type = switch (event.alertType()) {
            case MOISTURE_LOW -> NotificationType.IRRIGATION_ALERT;
            case SALINITY_HIGH -> NotificationType.EC_ALERT;
            case TEMPERATURE_HIGH -> NotificationType.TEMPERATURE_ALERT;
        };
        String title = switch (event.alertType()) {
            case MOISTURE_LOW -> "Water stress alert";
            case SALINITY_HIGH -> "Critical salinity alert";
            case TEMPERATURE_HIGH -> "Critical temperature alert";
        };
        String body = switch (event.alertType()) {
            case MOISTURE_LOW -> "Water stress in %s: soil moisture at %.1f%%.".formatted(zoneName, event.value());
            case SALINITY_HIGH -> "Critical salinity in %s: EC at %.2f dS/m.".formatted(zoneName, event.value());
            case TEMPERATURE_HIGH -> "Critical soil temperature in %s: %.1f°C.".formatted(zoneName, event.value());
        };
        dispatchPushToAllDevices(userId, type, title, body, event.alertId(), "ALERT");
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onIntrusionDetected(IntrusionDetectedEvent event) {
        // Matches EP-003-US001: only a PERSON classification (severity CRITICAL) pushes a notification;
        // WIND is filtered out entirely and ANIMAL is recorded without alerting the farmer.
        if (event.classification() != EventClassification.PERSON) return;
        Long userId = resolveUserIdByFarmId(event.farmId());
        if (userId == null) {
            log.warn("Cannot dispatch push for securityEventId={}: farm owner not found (farmId={})", event.eventId(), event.farmId());
            return;
        }
        String location = event.locationDescription() != null ? event.locationDescription() : "your parcel";
        dispatchPushToAllDevices(userId, NotificationType.SECURITY_ALERT, "Intrusion alert",
                "Person detected in %s".formatted(location), event.eventId(), "SECURITY_EVENT");
    }

    private Long resolveUserIdByFarmId(Long farmId) {
        if (farmId == null) return null;
        return farmRepository.findById(farmId).map(farm -> farm.getUserId()).orElse(null);
    }

    private void dispatchPushToAllDevices(Long userId, NotificationType type, String title, String body,
                                          Long relatedEntityId, String relatedEntityType) {
        var tokens = deviceTokenRepository.findByUserId(userId);
        if (tokens.isEmpty()) {
            log.info("No FCM tokens registered for userId={}, skipping push (type={})", userId, type);
            return;
        }
        for (var token : tokens) {
            var command = new SendNotificationCommand(userId, type, title, body, NotificationChannel.PUSH,
                    relatedEntityId, relatedEntityType, token.getFcmToken());
            notificationCommandService.sendNotification(command);
        }
    }
}
