package com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.aggregates;

import com.satecho.agrosafe.platform.irrigation.domain.model.valueobjects.ActuatorAction;
import com.satecho.agrosafe.platform.irrigation.domain.model.valueobjects.ActuatorType;
import com.satecho.agrosafe.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
public class ActuatorLog extends AuditableAbstractAggregateRoot<ActuatorLog> {
    @Setter private Long id;
    @Setter private Long deviceId;
    @Setter private Long zoneId;
    @Setter private ActuatorType actuatorType;
    @Setter private ActuatorAction action;
    @Setter private String commandSource;
    @Setter private Instant executedAt;
    @Setter private boolean success;
    @Setter private String responseMessage;

    public ActuatorLog() {}

    public ActuatorLog(Long deviceId, Long zoneId, ActuatorType actuatorType, ActuatorAction action,
                       String commandSource, boolean success, String responseMessage) {
        this.deviceId = deviceId;
        this.zoneId = zoneId;
        this.actuatorType = actuatorType;
        this.action = action;
        this.commandSource = commandSource;
        this.executedAt = Instant.now();
        this.success = success;
        this.responseMessage = responseMessage;
    }
}
