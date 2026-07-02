package com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.AlertSeverity;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.AlertStatus;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.AlertType;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "alerts")
@Getter
@Setter
@NoArgsConstructor
public class AlertPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Column(name = "zone_id", nullable = false)
    private Long zoneId;
    @Column(name = "device_id")
    private Long deviceId;
    @Column(name = "farm_id")
    private Long farmId;
    @Enumerated(EnumType.STRING)
    @Column(name = "alert_type", nullable = false, length = 30)
    private AlertType alertType;
    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false, length = 20)
    private AlertSeverity severity;
    @Column(name = "value")
    private Double value;
    @Column(name = "threshold_value")
    private Double thresholdValue;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private AlertStatus status;
    @Column(name = "alert_created_at", nullable = false)
    private Instant alertCreatedAt;
    @Column(name = "resolved_at")
    private Instant resolvedAt;
}
