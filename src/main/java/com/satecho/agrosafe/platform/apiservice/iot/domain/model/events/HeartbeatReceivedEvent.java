package com.satecho.agrosafe.platform.apiservice.iot.domain.model.events;

public record HeartbeatReceivedEvent(Long deviceId, Double batteryLevel) {}
