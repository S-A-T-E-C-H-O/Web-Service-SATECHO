package com.satecho.agrosafe.platform.apiservice.activitylog.application.internal.eventhandlers;

import com.satecho.agrosafe.platform.apiservice.activitylog.application.commandservices.ActivityLogCommandService;
import com.satecho.agrosafe.platform.apiservice.activitylog.domain.model.valueobjects.ActivityType;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.events.AlertCreatedEvent;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.events.IrrigationStartedEvent;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.events.IrrigationStoppedEvent;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.ZoneQueryService;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.events.ThresholdsUpdatedEvent;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.events.IntrusionDetectedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Bridges domain events from other bounded contexts into a chronological
 * activity log per farm (EP-004-US020). AFTER_COMMIT so a log entry is only
 * recorded once the originating change has actually persisted.
 */
@Component
public class ActivityLogEventHandler {

    private final ActivityLogCommandService activityLogCommandService;
    private final ZoneQueryService zoneQueryService;

    public ActivityLogEventHandler(ActivityLogCommandService activityLogCommandService, ZoneQueryService zoneQueryService) {
        this.activityLogCommandService = activityLogCommandService;
        this.zoneQueryService = zoneQueryService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(IrrigationStartedEvent event) {
        zoneQueryService.findById(event.zoneId()).ifPresent(zone ->
                activityLogCommandService.record(zone.getFarmId(), ActivityType.IRRIGATION,
                        "Irrigation started (" + event.durationMinutes() + " min)", event.startedAt()));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(IrrigationStoppedEvent event) {
        zoneQueryService.findById(event.zoneId()).ifPresent(zone ->
                activityLogCommandService.record(zone.getFarmId(), ActivityType.IRRIGATION,
                        "Irrigation stopped" + (event.totalWaterUsedLiters() != null
                                ? " (" + String.format("%.1f", event.totalWaterUsedLiters()) + " L used)" : ""),
                        event.stoppedAt()));
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(AlertCreatedEvent event) {
        activityLogCommandService.record(event.farmId(), ActivityType.ALERT,
                event.alertType() + " alert (" + event.severity() + ")", event.createdAt());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(IntrusionDetectedEvent event) {
        activityLogCommandService.record(event.farmId(), ActivityType.SECURITY,
                event.classification() + " detected" + (event.locationDescription() != null
                        ? " (" + event.locationDescription() + ")" : ""),
                event.detectedAt());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(ThresholdsUpdatedEvent event) {
        activityLogCommandService.record(event.farmId(), ActivityType.THRESHOLD,
                "Thresholds updated for zone " + event.zoneId(), event.updatedAt());
    }
}
