package com.satecho.agrosafe.platform.apiservice.iot.interfaces.rest.resources;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record BatchRegisterResource(@NotEmpty(message = "Devices list must not be empty") @Valid List<RegisterDeviceResource> devices) {}
