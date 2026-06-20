package com.satecho.agrosafe.platform.apiservice.soil.domain.model.events;

public record ThresholdBreachedEvent(Long readingId, Long deviceId, Long zoneId, String metricType, Double value, Double minThreshold, Double maxThreshold) {}
