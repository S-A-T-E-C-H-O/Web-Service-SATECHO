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
    @Setter private Boolean active;

    public Farm() {
        this.active = true;
    }

    public Farm(Long userId, String name, String location, double hectares, CropType cropType) {
        this.userId = userId;
        this.name = name;
        this.location = location;
        this.hectares = hectares;
        this.cropType = cropType;
        this.active = true;
    }

    public void update(String name, String location, double hectares, CropType cropType) {
        this.name = name;
        this.location = location;
        this.hectares = hectares;
        this.cropType = cropType;
    }

    public boolean isActive() {
        return active == null || active;
    }

    public void deactivate() {
        this.active = false;
    }

    public void reactivate() {
        this.active = true;
    }
}