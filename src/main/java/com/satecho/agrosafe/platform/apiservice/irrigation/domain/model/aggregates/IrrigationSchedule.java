package com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.valueobjects.RecurrencePattern;
import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.Instant;

@Getter
public class IrrigationSchedule extends AuditableAbstractAggregateRoot<IrrigationSchedule> {
    @Setter private Long id;
    @Setter private Long zoneId;
    @Setter private Long deviceId;
    @Setter private Instant startAt;
    @Setter private Integer durationMinutes;
    @Setter private RecurrencePattern recurrence;
    @Setter private String cronExpression;
    @Setter private Boolean enabled;
    @Setter private Instant nextRunAt;

    public IrrigationSchedule() {}

    public IrrigationSchedule(Long zoneId, Long deviceId, Instant startAt, Integer durationMinutes,
                              RecurrencePattern recurrence, String cronExpression, Boolean enabled) {
        if (zoneId == null) throw new IllegalArgumentException("zoneId cannot be null");
        if (deviceId == null) throw new IllegalArgumentException("deviceId cannot be null");
        if (startAt == null) throw new IllegalArgumentException("startAt cannot be null");
        if (durationMinutes == null || durationMinutes < 1) throw new IllegalArgumentException("durationMinutes must be at least 1");
        if (recurrence == null) throw new IllegalArgumentException("recurrence cannot be null");
        this.zoneId = zoneId;
        this.deviceId = deviceId;
        this.startAt = startAt;
        this.durationMinutes = durationMinutes;
        this.recurrence = recurrence;
        this.cronExpression = cronExpression;
        this.enabled = enabled != null ? enabled : true;
        this.nextRunAt = calculateNextRun();
    }

    public void update(Instant startAt, Integer durationMinutes, RecurrencePattern recurrence, String cronExpression, Boolean enabled) {
        if (startAt != null) this.startAt = startAt;
        if (durationMinutes != null && durationMinutes > 0) this.durationMinutes = durationMinutes;
        if (recurrence != null) this.recurrence = recurrence;
        if (cronExpression != null) this.cronExpression = cronExpression;
        if (enabled != null) this.enabled = enabled;
        this.nextRunAt = calculateNextRun();
    }

    public void disable() { this.enabled = false; this.nextRunAt = null; }
    public void enable() { this.enabled = true; this.nextRunAt = calculateNextRun(); }

    public Instant calculateNextRun() {
        if (!this.enabled) return null;
        if (this.recurrence == RecurrencePattern.ONCE) return this.startAt;
        Instant now = Instant.now();
        Instant candidate = this.startAt;
        while (candidate.isBefore(now)) {
            candidate = switch (this.recurrence) {
                case DAILY -> candidate.plus(Duration.ofDays(1));
                case WEEKLY -> candidate.plus(Duration.ofDays(7));
                case CUSTOM -> { yield this.startAt; }
                default -> throw new IllegalStateException("Unhandled recurrence: " + this.recurrence);
            };
        }
        return candidate;
    }
}
