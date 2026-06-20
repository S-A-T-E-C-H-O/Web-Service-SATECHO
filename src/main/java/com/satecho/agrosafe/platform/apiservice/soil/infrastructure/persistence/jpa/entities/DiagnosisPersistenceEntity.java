package com.satecho.agrosafe.platform.apiservice.soil.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "diagnoses")
@Getter
@Setter
@NoArgsConstructor
public class DiagnosisPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Column(name = "zone_id", nullable = false)
    private Long zoneId;
    @Column(name = "water_stress_index")
    private Double waterStressIndex;
    @Column(name = "soil_health_score")
    private Double soilHealthScore;
    @Column(length = 2000)
    private String recommendations;
    @Column(name = "moisture_status", length = 50)
    private String moistureStatus;
    @Column(name = "ec_status", length = 50)
    private String ecStatus;
    @Column(name = "ph_status", length = 50)
    private String phStatus;
    @Column(name = "temperature_status", length = 50)
    private String temperatureStatus;
    @Column(name = "generated_at", nullable = false)
    private Instant generatedAt;
}
