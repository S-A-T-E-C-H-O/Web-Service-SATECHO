package com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.resources;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.valueobjects.CropType;

import java.util.List;

public record CreateFarmResource(String name, String location, double hectares, CropType cropType, List<CreateZoneResource> zones) {}