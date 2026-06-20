package com.satecho.agrosafe.platform.apiservice.iot.domain.model.events;

public record DeviceDecommissionedEvent(Long deviceId, String serialNumber) {}
