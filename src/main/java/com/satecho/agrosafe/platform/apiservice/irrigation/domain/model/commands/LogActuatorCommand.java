package com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.commands;

import com.satecho.agrosafe.platform.irrigation.domain.model.valueobjects.ActuatorAction;
import com.satecho.agrosafe.platform.irrigation.domain.model.valueobjects.ActuatorType;

public record LogActuatorCommand(Long deviceId, Long zoneId, ActuatorType actuatorType, ActuatorAction action,
                                 String commandSource, boolean success, String responseMessage) {}
