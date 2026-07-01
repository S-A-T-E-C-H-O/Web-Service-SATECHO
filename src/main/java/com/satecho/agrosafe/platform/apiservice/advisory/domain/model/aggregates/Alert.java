package com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.AlertSeverity;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.AlertStatus;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.AlertType;
import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
public class Alert extends AuditableAbstractAggregateRoot<Alert> {
    @Setter private Long id;
    @Setter private Long zoneId;
    @Setter private Long deviceId;
    @Setter private Long farmId;
    @Setter private AlertType alertType;
    @Setter private AlertSeverity severity;
    @Setter private Double value;
    @Setter private Double thresholdValue;
    @Setter private AlertStatus status;
    @Setter private Instant createdAt;
    @Setter private Instant resolvedAt;

    public Alert() {}

    public Alert(Long zoneId, Long deviceId, Long farmId, AlertType alertType, AlertSeverity severity,
                 Double value, Double thresholdValue) {
        if (zoneId == null) throw new IllegalArgumentException("zoneId cannot be null");
        if (alertType == null) throw new IllegalArgumentException("alertType cannot be null");
        if (severity == null) throw new IllegalArgumentException("severity cannot be null");
        this.zoneId = zoneId;
        this.deviceId = deviceId;
        this.farmId = farmId;
        this.alertType = alertType;
        this.severity = severity;
        this.value = value;
        this.thresholdValue = thresholdValue;
        this.status = AlertStatus.ACTIVE;
        this.createdAt = Instant.now();
    }

    public void resolve() {
        if (this.status != AlertStatus.ACTIVE) throw new IllegalStateException("Alert is not active");
        this.status = AlertStatus.RESOLVED;
        this.resolvedAt = Instant.now();
    }

    public boolean isActive() {
        return this.status == AlertStatus.ACTIVE;
    }
}