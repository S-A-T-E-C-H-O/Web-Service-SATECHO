package com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.events;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.valueobjects.CropType;

public class FarmCreatedEvent extends org.springframework.context.ApplicationEvent {
    private final Long farmId;
    private final Long userId;
    private final String farmName;
    private final CropType cropType;
    private final double hectares;
    private final String location;

    public FarmCreatedEvent(Object source, Long farmId, Long userId, String farmName,
                            CropType cropType, double hectares, String location) {
        super(source);
        this.farmId = farmId;
        this.userId = userId;
        this.farmName = farmName;
        this.cropType = cropType;
        this.hectares = hectares;
        this.location = location;
    }

    public Long getFarmId() { return farmId; }
    public Long getUserId() { return userId; }
    public String getFarmName() { return farmName; }
    public CropType getCropType() { return cropType; }
    public double getHectares() { return hectares; }
    public String getLocation() { return location; }
}