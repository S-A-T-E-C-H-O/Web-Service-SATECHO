package com.satecho.agrosafe.platform.apiservice.soil.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
public class Diagnosis extends AuditableAbstractAggregateRoot<Diagnosis> {
    @Setter private Long id;
    @Setter private Long zoneId;
    @Setter private Double waterStressIndex;
    @Setter private Double soilHealthScore;
    @Setter private String recommendations;
    @Setter private String moistureStatus;
    @Setter private String ecStatus;
    @Setter private String phStatus;
    @Setter private String temperatureStatus;
    @Setter private Instant generatedAt;

    public Diagnosis() {}

    public Diagnosis(Long zoneId, Double waterStressIndex, Double soilHealthScore,
                     String recommendations, String moistureStatus, String ecStatus,
                     String phStatus, String temperatureStatus) {
        this.zoneId = zoneId;
        this.waterStressIndex = waterStressIndex;
        this.soilHealthScore = soilHealthScore;
        this.recommendations = recommendations;
        this.moistureStatus = moistureStatus;
        this.ecStatus = ecStatus;
        this.phStatus = phStatus;
        this.temperatureStatus = temperatureStatus;
        this.generatedAt = Instant.now();
    }
}
