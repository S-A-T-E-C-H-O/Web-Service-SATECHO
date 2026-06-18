package com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.aggregates;

import com.satecho.agrosafe.platform.irrigation.domain.model.valueobjects.IrrigationStatus;
import com.satecho.agrosafe.platform.irrigation.domain.model.valueobjects.SessionResult;
import com.satecho.agrosafe.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;

@Getter
public class IrrigationSession extends AuditableAbstractAggregateRoot<IrrigationSession> {
    @Setter private Long id;
    @Setter private Long farmId;
    @Setter private Long zoneId;
    @Setter private Long deviceId;
    @Setter private IrrigationStatus status;
    @Setter private Instant startedAt;
    @Setter private Instant stoppedAt;
    @Setter private Integer durationMinutes;
    @Setter private Double totalWaterUsedLiters;
    @Setter private String triggeredBy;
    @Setter private SessionResult sessionResult;

    public IrrigationSession() {}

    public IrrigationSession(Long farmId, Long zoneId, Long deviceId, String triggeredBy, Integer durationMinutes) {
        if (zoneId == null) throw new IllegalArgumentException("zoneId cannot be null");
        if (deviceId == null) throw new IllegalArgumentException("deviceId cannot be null");
        if (triggeredBy == null || triggeredBy.isBlank()) throw new IllegalArgumentException("triggeredBy cannot be null or blank");
        this.farmId = farmId;
        this.zoneId = zoneId;
        this.deviceId = deviceId;
        this.status = IrrigationStatus.ACTIVE;
        this.startedAt = Instant.now();
        this.triggeredBy = triggeredBy;
        this.durationMinutes = durationMinutes;
    }

    public void stop(Double flowRateLitersPerMinute) {
        if (this.status != IrrigationStatus.ACTIVE) throw new IllegalStateException("Cannot stop session that is not ACTIVE");
        this.stoppedAt = Instant.now();
        int actualDuration = (int) Duration.between(this.startedAt, this.stoppedAt).toMinutes();
        this.durationMinutes = actualDuration;
        this.totalWaterUsedLiters = actualDuration * flowRateLitersPerMinute;
        this.status = IrrigationStatus.STOPPED;
        this.sessionResult = new SessionResult(this.totalWaterUsedLiters, null, this.durationMinutes);
    }

    public void complete(Double flowRateLitersPerMinute) {
        if (this.status != IrrigationStatus.ACTIVE) throw new IllegalStateException("Cannot complete session that is not ACTIVE");
        this.stoppedAt = Instant.now();
        int actualDuration = (int) Duration.between(this.startedAt, this.stoppedAt).toMinutes();
        this.durationMinutes = actualDuration;
        this.totalWaterUsedLiters = actualDuration * flowRateLitersPerMinute;
        this.status = IrrigationStatus.COMPLETED;
        this.sessionResult = new SessionResult(this.totalWaterUsedLiters, null, this.durationMinutes);
    }

    public void markFailed(String reason) {
        this.status = IrrigationStatus.FAILED;
        this.stoppedAt = Instant.now();
    }

    public boolean isActive() { return this.status == IrrigationStatus.ACTIVE; }
}
