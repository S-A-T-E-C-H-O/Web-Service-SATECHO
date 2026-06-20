package com.satecho.agrosafe.platform.apiservice.security.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.security.domain.model.valueobjects.EventClassification;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.valueobjects.EventSeverity;
import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
public class SecurityEvent extends AuditableAbstractAggregateRoot<SecurityEvent> {
    @Setter private Long id;
    @Setter private Long farmId;
    @Setter private Long deviceId;
    @Setter private EventClassification classification;
    @Setter private Double confidenceLevel;
    @Setter private EventSeverity severity;
    @Setter private Instant detectedAt;
    @Setter private Boolean acknowledged;
    @Setter private Long acknowledgedBy;
    @Setter private Instant acknowledgedAt;
    @Setter private String locationDescription;
    @Setter private String rawData;

    public SecurityEvent() {}

    public SecurityEvent(Long farmId, Long deviceId, EventClassification classification,
                         Double confidenceLevel, Instant detectedAt, String locationDescription, String rawData) {
        if (farmId == null) throw new IllegalArgumentException("farmId cannot be null");
        if (deviceId == null) throw new IllegalArgumentException("deviceId cannot be null");
        if (classification == null) throw new IllegalArgumentException("classification cannot be null");
        if (confidenceLevel == null) throw new IllegalArgumentException("confidenceLevel cannot be null");
        if (detectedAt == null) throw new IllegalArgumentException("detectedAt cannot be null");
        this.farmId = farmId;
        this.deviceId = deviceId;
        this.classification = classification;
        this.confidenceLevel = Math.max(0.0, Math.min(100.0, confidenceLevel));
        this.severity = determineSeverity(classification);
        this.detectedAt = detectedAt;
        this.acknowledged = false;
        this.locationDescription = locationDescription;
        this.rawData = rawData;
    }

    public void acknowledge(Long acknowledgedBy) {
        if (acknowledgedBy == null) throw new IllegalArgumentException("acknowledgedBy cannot be null");
        if (Boolean.TRUE.equals(this.acknowledged)) throw new IllegalStateException("Security event is already acknowledged");
        this.acknowledged = true;
        this.acknowledgedBy = acknowledgedBy;
        this.acknowledgedAt = Instant.now();
    }

    private EventSeverity determineSeverity(EventClassification classification) {
        return switch (classification) {
            case PERSON -> EventSeverity.CRITICAL;
            case ANIMAL -> EventSeverity.MEDIUM;
            case WIND -> EventSeverity.LOW;
            case UNKNOWN -> EventSeverity.HIGH;
        };
    }
}
