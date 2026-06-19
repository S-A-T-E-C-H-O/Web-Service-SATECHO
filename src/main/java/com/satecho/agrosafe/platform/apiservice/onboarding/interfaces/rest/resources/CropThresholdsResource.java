package com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.resources;

public record CropThresholdsResource(String name, String displayName, double minMoisture, double maxMoisture, double minEc, double maxEc, double minPh, double maxPh, double minTemperature, double maxTemperature) {}