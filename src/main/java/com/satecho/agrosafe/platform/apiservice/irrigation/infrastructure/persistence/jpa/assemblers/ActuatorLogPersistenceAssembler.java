package com.satecho.agrosafe.platform.apiservice.irrigation.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.irrigation.domain.model.aggregates.ActuatorLog;
import com.satecho.agrosafe.platform.irrigation.infrastructure.persistence.jpa.entities.ActuatorLogPersistenceEntity;

public final class ActuatorLogPersistenceAssembler {
    private ActuatorLogPersistenceAssembler() {}

    public static ActuatorLog toDomainFromPersistence(ActuatorLogPersistenceEntity e) {
        if (e == null) return null;
        var d = new ActuatorLog();
        d.setId(e.getId());
        d.setDeviceId(e.getDeviceId());
        d.setZoneId(e.getZoneId());
        d.setActuatorType(e.getActuatorType());
        d.setAction(e.getAction());
        d.setCommandSource(e.getCommandSource());
        d.setExecutedAt(e.getExecutedAt());
        d.setSuccess(e.isSuccess());
        d.setResponseMessage(e.getResponseMessage());
        return d;
    }

    public static ActuatorLogPersistenceEntity toPersistenceFromDomain(ActuatorLog d) {
        if (d == null) return null;
        var e = new ActuatorLogPersistenceEntity();
        if (d.getId() != null) e.setId(d.getId());
        e.setDeviceId(d.getDeviceId());
        e.setZoneId(d.getZoneId());
        e.setActuatorType(d.getActuatorType());
        e.setAction(d.getAction());
        e.setCommandSource(d.getCommandSource());
        e.setExecutedAt(d.getExecutedAt());
        e.setSuccess(d.isSuccess());
        e.setResponseMessage(d.getResponseMessage());
        return e;
    }
}
