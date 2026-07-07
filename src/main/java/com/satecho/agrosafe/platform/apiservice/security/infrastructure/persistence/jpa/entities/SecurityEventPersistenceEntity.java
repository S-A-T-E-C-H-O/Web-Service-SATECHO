package com.satecho.agrosafe.platform.apiservice.security.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.security.domain.model.valueobjects.EventClassification;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.valueobjects.EventSeverity;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "security_events")
@Getter
@Setter
@NoArgsConstructor
public class SecurityEventPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Column(name = "farm_id", nullable = false)
    private Long farmId;
    @Column(name = "device_id", nullable = false)
    private Long deviceId;
    @Column(name = "zone_id")
    private Long zoneId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EventClassification classification;
    @Column(name = "confidence_level", nullable = false)
    private Double confidenceLevel;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EventSeverity severity;
    @Column(name = "detected_at", nullable = false)
    private Instant detectedAt;
    @Column(nullable = false)
    private Boolean acknowledged;
    @Column(name = "acknowledged_by")
    private Long acknowledgedBy;
    @Column(name = "acknowledged_at")
    private Instant acknowledgedAt;
    @Column(name = "location_description", length = 500)
    private String locationDescription;
    @Column(name = "raw_data", columnDefinition = "TEXT")
    private String rawData;
    @Column(name = "pulse_duration_ms")
    private Double pulseDurationMs;
    @Column(name = "triggers_per_minute")
    private Integer triggersPerMinute;
}
