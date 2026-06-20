package com.satecho.agrosafe.platform.apiservice.soil.interfaces.rest.resources;

import java.time.Instant;

public record DiagnosisResource(Long id, Long zoneId, Double waterStressIndex, Double soilHealthScore, String recommendations, String moistureStatus, String ecStatus, String phStatus, String temperatureStatus, Instant generatedAt) {}
