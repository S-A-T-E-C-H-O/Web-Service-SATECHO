package com.satecho.agrosafe.platform.apiservice.irrigation.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.irrigation.domain.model.valueobjects.RecurrencePattern;
import com.satecho.agrosafe.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "irrigation_schedules")
@Getter
@Setter
@NoArgsConstructor
public class IrrigationSchedulePersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Column(name = "zone_id", nullable = false)
    private Long zoneId;
    @Column(name = "device_id", nullable = false)
    private Long deviceId;
    @Column(name = "start_at", nullable = false)
    private Instant startAt;
    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RecurrencePattern recurrence;
    @Column(name = "cron_expression", length = 100)
    private String cronExpression;
    @Column(nullable = false)
    private Boolean enabled;
    @Column(name = "next_run_at")
    private Instant nextRunAt;
}
