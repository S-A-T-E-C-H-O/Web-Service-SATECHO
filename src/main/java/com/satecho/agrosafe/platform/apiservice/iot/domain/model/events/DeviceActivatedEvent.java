package com.satecho.agrosafe.platform.apiservice.iot.domain.model.events;

public record DeviceActivatedEvent(Long deviceId, String serialNumber) {}
