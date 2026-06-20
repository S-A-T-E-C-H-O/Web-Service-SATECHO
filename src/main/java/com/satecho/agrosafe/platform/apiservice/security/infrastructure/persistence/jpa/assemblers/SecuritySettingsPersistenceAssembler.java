package com.satecho.agrosafe.platform.apiservice.security.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.security.domain.model.aggregates.SecuritySettings;
import com.satecho.agrosafe.platform.apiservice.security.infrastructure.persistence.jpa.entities.SecuritySettingsPersistenceEntity;

public final class SecuritySettingsPersistenceAssembler {
    private SecuritySettingsPersistenceAssembler() {}

    public static SecuritySettings toDomainFromPersistence(SecuritySettingsPersistenceEntity e) {
        if (e == null) return null;
        var d = new SecuritySettings();
        d.setId(e.getId());
        d.setFarmId(e.getFarmId());
        d.setMotionSensitivity(e.getMotionSensitivity());
        d.setAlertMode(e.getAlertMode());
        d.setDetectionScheduleStart(e.getDetectionScheduleStart());
        d.setDetectionScheduleEnd(e.getDetectionScheduleEnd());
        d.setNotificationContacts(e.getNotificationContacts());
        return d;
    }

    public static SecuritySettingsPersistenceEntity toPersistenceFromDomain(SecuritySettings d) {
        if (d == null) return null;
        var e = new SecuritySettingsPersistenceEntity();
        if (d.getId() != null) e.setId(d.getId());
        e.setFarmId(d.getFarmId());
        e.setMotionSensitivity(d.getMotionSensitivity());
        e.setAlertMode(d.getAlertMode());
        e.setDetectionScheduleStart(d.getDetectionScheduleStart());
        e.setDetectionScheduleEnd(d.getDetectionScheduleEnd());
        e.setNotificationContacts(d.getNotificationContacts());
        return e;
    }
}
