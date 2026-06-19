package com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.resources;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.valueobjects.CropType;

public record CreateZoneResource(String name, double areaHectares, CropType cropType) {}