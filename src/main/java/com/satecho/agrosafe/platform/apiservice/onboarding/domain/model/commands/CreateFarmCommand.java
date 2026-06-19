package com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.commands;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.valueobjects.CropType;

public record CreateFarmCommand(Long userId, String name, String location, double hectares, CropType cropType) {
    public CreateFarmCommand {
        if (userId == null) throw new IllegalArgumentException("User ID is required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Farm name is required");
        if (hectares <= 0) throw new IllegalArgumentException("Hectares must be greater than 0");
        if (cropType == null) throw new IllegalArgumentException("Crop type is required");
    }
}