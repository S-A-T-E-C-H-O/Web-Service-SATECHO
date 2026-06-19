package com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.valueobjects.CropType;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.valueobjects.ThresholdLimits;
import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

@Getter
public class IrrigationZone extends AuditableAbstractAggregateRoot<IrrigationZone> {

    @Setter private Long id;
    @Setter private Long farmId;
    @Setter private String name;
    @Setter private double areaHectares;
    @Setter private CropType cropType;
    @Setter private Long deviceId;
    @Setter private ThresholdLimits thresholds;

    public IrrigationZone() {
    }

    public IrrigationZone(Long farmId, String name, double areaHectares, CropType cropType) {
        this.farmId = farmId;
        this.name = name;
        this.areaHectares = areaHectares;
        this.cropType = cropType;
        this.thresholds = CropType.getDefaultThresholdsFor(cropType);
    }

    public void update(String name, double areaHectares, CropType cropType) {
        this.name = name;
        this.areaHectares = areaHectares;
        this.cropType = cropType;
    }

    public void updateThresholds(ThresholdLimits thresholds) {
        this.thresholds = thresholds;
    }

    public void linkDevice(Long deviceId) {
        this.deviceId = deviceId;
    }
}