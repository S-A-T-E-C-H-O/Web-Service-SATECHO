package com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.valueobjects.CropType;
import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Farm extends AuditableAbstractAggregateRoot<Farm> {

    @Setter private Long id;
    @Setter private Long userId;
    @Setter private String name;
    @Setter private String location;
    @Setter private double hectares;
    @Setter private CropType cropType;

    public Farm() {
    }

    public Farm(Long userId, String name, String location, double hectares, CropType cropType) {
        this.userId = userId;
        this.name = name;
        this.location = location;
        this.hectares = hectares;
        this.cropType = cropType;
    }

    public void update(String name, String location, double hectares, CropType cropType) {
        this.name = name;
        this.location = location;
        this.hectares = hectares;
        this.cropType = cropType;
    }
}