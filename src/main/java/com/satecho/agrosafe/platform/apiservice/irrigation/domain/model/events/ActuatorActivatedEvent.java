package com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.events;

import com.satecho.agrosafe.platform.irrigation.domain.model.valueobjects.ActuatorAction;
import com.satecho.agrosafe.platform.irrigation.domain.model.valueobjects.ActuatorType;
import java.time.Instant;

public record ActuatorActivatedEvent(Long logId, Long deviceId, Long zoneId, ActuatorType actuatorType,
                                     ActuatorAction action, String commandSource, boolean success, Instant executedAt) {}
