package com.satecho.agrosafe.platform.apiservice.onboarding.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.valueobjects.CropType;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "irrigation_zones")
@Getter
@Setter
@NoArgsConstructor
public class IrrigationZonePersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "farm_id", nullable = false)
    private Long farmId;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(name = "area_hectares", nullable = false)
    private double areaHectares;

    @Enumerated(EnumType.STRING)
    @Column(name = "crop_type", nullable = false, length = 30)
    private CropType cropType;

    @Column(name = "device_id")
    private Long deviceId;

    @Embedded
    private ThresholdLimitsEmbeddable thresholds;
}