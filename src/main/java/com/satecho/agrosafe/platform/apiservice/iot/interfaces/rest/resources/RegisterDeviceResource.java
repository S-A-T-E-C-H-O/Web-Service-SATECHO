package com.satecho.agrosafe.platform.apiservice.iot.interfaces.rest.resources;

import com.satecho.agrosafe.platform.apiservice.iot.domain.model.valueobjects.DeviceType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Devices are provisioned by SATECHO fleet administrators, never self-registered
 * by farmers — farmers only ever see device status once the hardware they were
 * given starts reporting data. See {@code farmerId}: the owner is specified by
 * the admin performing the provisioning, not inferred from the caller.
 */
public record RegisterDeviceResource(
        @NotBlank(message = "Serial number is required") String serialNumber,
        @NotNull(message = "Device type is required") DeviceType type,
        @NotNull(message = "farmerId is required") Long farmerId
) {}
