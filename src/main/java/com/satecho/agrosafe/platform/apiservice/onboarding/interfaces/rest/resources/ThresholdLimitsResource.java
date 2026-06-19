package com.satecho.agrosafe.platform.apiservice.onboarding.interfaces.rest.resources;

public record ThresholdLimitsResource(double minMoisture, double maxMoisture, double minEc, double maxEc, double minPh, double maxPh, double minTemperature, double maxTemperature) {}