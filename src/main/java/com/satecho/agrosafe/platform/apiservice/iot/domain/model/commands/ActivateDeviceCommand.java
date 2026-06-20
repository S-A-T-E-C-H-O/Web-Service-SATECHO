package com.satecho.agrosafe.platform.apiservice.iot.domain.model.commands;

public record ActivateDeviceCommand(Long deviceId, String certificateThumbprint) {}
