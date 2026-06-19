package com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.valueobjects;

public record ThresholdLimits(
        double minMoisture,
        double maxMoisture,
        double minEc,
        double maxEc,
        double minPh,
        double maxPh,
        double minTemperature,
        double maxTemperature
) {
}