package com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.commands;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.valueobjects.CropType;

public record CreateZoneCommand(Long farmId, String name, double areaHectares, CropType cropType) {
    public CreateZoneCommand {
        if (farmId == null) throw new IllegalArgumentException("Farm ID is required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Zone name is required");
        if (areaHectares <= 0) throw new IllegalArgumentException("Area hectares must be greater than 0");
        if (cropType == null) throw new IllegalArgumentException("Crop type is required");
    }
}