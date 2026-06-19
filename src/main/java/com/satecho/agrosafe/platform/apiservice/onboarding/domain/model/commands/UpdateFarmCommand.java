package com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.commands;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.valueobjects.CropType;

public record UpdateFarmCommand(String name, String location, double hectares, CropType cropType) {
    public UpdateFarmCommand {
        if (name != null && name.isBlank()) throw new IllegalArgumentException("Farm name cannot be blank");
        if (hectares < 0) throw new IllegalArgumentException("Hectares cannot be negative");
    }
}