package com.satecho.agrosafe.platform.apiservice.advisory.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.advisory.application.commandservices.AlertCommandService;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.Alert;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.events.AlertCreatedEvent;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.AlertSeverity;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.AlertStatus;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.AlertType;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.repositories.AlertRepository;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.valueobjects.ThresholdLimits;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.repositories.ZoneRepository;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.events.TelemetryReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Consumes {@link TelemetryReceivedEvent}, which is published for every soil reading
 * (not just breaches), so hysteresis can be evaluated: an active alert auto-resolves
 * the moment a later reading falls back within the zone's configured thresholds.
 */
@Service
public class AlertCommandServiceImpl implements AlertCommandService {

    private static final Logger log = LoggerFactory.getLogger(AlertCommandServiceImpl.class);

    private final AlertRepository alertRepository;
    private final ZoneRepository zoneRepository;
    private final ApplicationEventPublisher eventPublisher;

    public AlertCommandServiceImpl(AlertRepository alertRepository, ZoneRepository zoneRepository,
                                    ApplicationEventPublisher eventPublisher) {
        this.alertRepository = alertRepository;
        this.zoneRepository = zoneRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void evaluateReading(TelemetryReceivedEvent event) {
        AlertType alertType = resolveAlertType(event.metricType());
        if (alertType == null || event.zoneId() == null) return;

        var zone = zoneRepository.findById(event.zoneId()).orElse(null);
        if (zone == null || zone.getThresholds() == null) return;

        Double breachedThreshold = evaluateBreach(alertType, zone.getThresholds(), event.value());
        var existingActive = alertRepository.findByZoneIdAndAlertTypeAndStatus(event.zoneId(), alertType, AlertStatus.ACTIVE);

        if (breachedThreshold != null) {
            if (existingActive.isPresent()) return;
            var alert = new Alert(event.zoneId(), event.deviceId(), zone.getFarmId(), alertType,
                    AlertSeverity.CRITICAL, event.value(), breachedThreshold);
            var saved = alertRepository.save(alert);
            log.info("Alert created: type={} zoneId={} value={} threshold={}", alertType, event.zoneId(), event.value(), breachedThreshold);
            eventPublisher.publishEvent(new AlertCreatedEvent(saved.getId(), saved.getZoneId(), saved.getDeviceId(),
                    saved.getFarmId(), saved.getAlertType(), saved.getSeverity(), saved.getValue(), saved.getCreatedAt()));
        } else {
            existingActive.ifPresent(alert -> {
                alert.resolve();
                alertRepository.save(alert);
                log.info("Alert resolved: id={} type={} zoneId={}", alert.getId(), alertType, event.zoneId());
            });
        }
    }

    private AlertType resolveAlertType(String metricType) {
        if (metricType == null) return null;
        return switch (metricType) {
            case "SOIL_MOISTURE" -> AlertType.MOISTURE_LOW;
            case "ELECTRICAL_CONDUCTIVITY" -> AlertType.SALINITY_HIGH;
            case "SOIL_TEMPERATURE" -> AlertType.TEMPERATURE_HIGH;
            default -> null;
        };
    }

    /**
     * Returns the breached threshold value if {@code value} is out of the safe range, or {@code null}
     * if it is within range (used both to detect a new breach and to detect recovery for hysteresis).
     */
    private Double evaluateBreach(AlertType alertType, ThresholdLimits thresholds, Double value) {
        if (value == null) return null;
        return switch (alertType) {
            case MOISTURE_LOW -> value < thresholds.minMoisture() ? thresholds.minMoisture() : null;
            case SALINITY_HIGH -> value > thresholds.maxEc() ? thresholds.maxEc() : null;
            case TEMPERATURE_HIGH -> value > thresholds.maxTemperature() ? thresholds.maxTemperature() : null;
        };
    }
}
