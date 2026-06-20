package com.satecho.agrosafe.platform.apiservice.iot.domain.model.events;

public record DeviceRegisteredEvent(Long deviceId, String serialNumber, String deviceType, Long userId) {}
