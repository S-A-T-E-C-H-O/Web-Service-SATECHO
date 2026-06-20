package com.satecho.agrosafe.platform.apiservice.iot.domain.model.queries;

import com.satecho.agrosafe.platform.apiservice.iot.domain.model.valueobjects.DeviceStatus;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.valueobjects.DeviceType;

public record GetAllDevicesByUserIdQuery(Long userId, DeviceType type, DeviceStatus status) {}
