package com.satecho.agrosafe.platform.apiservice.irrigation.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.valueobjects.ActuatorAction;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.valueobjects.ActuatorType;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "actuator_logs")
@Getter
@Setter
@NoArgsConstructor
public class ActuatorLogPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Column(name = "device_id", nullable = false)
    private Long deviceId;
    @Column(name = "zone_id", nullable = false)
    private Long zoneId;
    @Enumerated(EnumType.STRING)
    @Column(name = "actuator_type", nullable = false, length = 20)
    private ActuatorType actuatorType;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ActuatorAction action;
    @Column(name = "command_source", nullable = false, length = 20)
    private String commandSource;
    @Column(name = "executed_at", nullable = false)
    private Instant executedAt;
    @Column(nullable = false)
    private boolean success;
    @Column(name = "response_message", length = 500)
    private String responseMessage;
}
