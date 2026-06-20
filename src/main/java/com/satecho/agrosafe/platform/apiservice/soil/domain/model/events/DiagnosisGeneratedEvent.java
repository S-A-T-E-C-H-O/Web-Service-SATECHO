package com.satecho.agrosafe.platform.apiservice.soil.domain.model.events;

import java.time.Instant;

public record DiagnosisGeneratedEvent(Long diagnosisId, Long zoneId, Double waterStressIndex, Double soilHealthScore, Instant generatedAt) {}
