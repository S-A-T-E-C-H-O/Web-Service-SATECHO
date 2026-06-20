package com.satecho.agrosafe.platform.apiservice.iot.domain.model.commands;

public record UpdateFirmwareCommand(Long deviceId, String firmwareVersion) {}
