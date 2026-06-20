package com.satecho.agrosafe.platform.apiservice.soil.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "sensor_readings")
@Getter
@Setter
@NoArgsConstructor
public class SensorReadingPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Column(name = "device_id", nullable = false)
    private Long deviceId;

    @Column(name = "zone_id")
    private Long zoneId;

    @Embedded
    private ReadingValueEmbeddable readingValue;

    @Column(name = "sensor_timestamp", nullable = false)
    private Instant timestamp;

    @Column(name = "ingested_at", nullable = false)
    private Instant ingestedAt;

    @Column(name = "is_valid", nullable = false)
    private Boolean isValid;
}
