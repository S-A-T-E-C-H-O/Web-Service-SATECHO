package com.satecho.agrosafe.platform.apiservice.iot.interfaces.rest.resources;

import com.satecho.agrosafe.platform.apiservice.iot.domain.model.valueobjects.DeviceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegisterDeviceResource(
        @NotBlank(message = "Serial number is required") String serialNumber,
        @NotNull(message = "Device type is required") DeviceType type
) {}
