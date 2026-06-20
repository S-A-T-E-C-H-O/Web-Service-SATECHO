package com.satecho.agrosafe.platform.apiservice.iot.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public record ActivateDeviceResource(@NotBlank(message = "Certificate thumbprint is required") String certificateThumbprint) {}
