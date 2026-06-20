package com.satecho.agrosafe.platform.apiservice.iot.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record UpdateFirmwareResource(@NotBlank(message = "Firmware version is required") String firmwareVersion) {}
