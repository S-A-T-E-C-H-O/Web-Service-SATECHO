package com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.valueobjects;

public record SessionResult(Double totalWaterUsedLiters, Double waterSavedLiters, Integer durationMinutes) {}
