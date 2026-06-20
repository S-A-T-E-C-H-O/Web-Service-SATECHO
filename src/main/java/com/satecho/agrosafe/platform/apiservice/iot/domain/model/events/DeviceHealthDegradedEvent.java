package com.satecho.agrosafe.platform.apiservice.iot.domain.model.events;

public record DeviceHealthDegradedEvent(Long deviceId, String healthStatus) {}
