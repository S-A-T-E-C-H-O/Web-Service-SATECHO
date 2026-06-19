package com.satecho.agrosafe.platform.apiservice.irrigation.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.valueobjects.IrrigationStatus;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "irrigation_sessions")
@Getter
@Setter
@NoArgsConstructor
public class IrrigationSessionPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Column(nullable = false)
    private Long farmId;
    @Column(nullable = false)
    private Long zoneId;
    @Column(nullable = false)
    private Long deviceId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private IrrigationStatus status;
    @Column(nullable = false)
    private Instant startedAt;
    private Instant stoppedAt;
    private Integer durationMinutes;
    @Column(name = "total_water_used_liters")
    private Double totalWaterUsedLiters;
    @Column(nullable = false, length = 20)
    private String triggeredBy;
    @Embedded
    private SessionResultEmbeddable sessionResult;
}
