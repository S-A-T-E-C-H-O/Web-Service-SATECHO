package com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.resources;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.valueobjects.CropType;

public record UpdateFarmResource(String name, String location, double hectares, CropType cropType) {}