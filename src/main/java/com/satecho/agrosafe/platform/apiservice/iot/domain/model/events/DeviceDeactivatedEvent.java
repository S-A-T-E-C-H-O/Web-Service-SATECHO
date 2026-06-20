package com.satecho.agrosafe.platform.apiservice.iot.domain.model.events;

public record DeviceDeactivatedEvent(Long deviceId, String serialNumber) {}
