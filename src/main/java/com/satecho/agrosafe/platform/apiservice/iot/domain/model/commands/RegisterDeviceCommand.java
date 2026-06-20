package com.satecho.agrosafe.platform.apiservice.iot.domain.model.commands;

import com.satecho.agrosafe.platform.apiservice.iot.domain.model.valueobjects.DeviceType;

public record RegisterDeviceCommand(String serialNumber, DeviceType type, Long userId) {}
