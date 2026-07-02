package com.satecho.agrosafe.platform.apiservice.security.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.security.domain.model.aggregates.SecuritySettings;
import com.satecho.agrosafe.platform.apiservice.security.interfaces.rest.resources.SecuritySettingsResource;

public class SecuritySettingsResourceFromEntityAssembler {
    private SecuritySettingsResourceFromEntityAssembler() {}
    public static SecuritySettingsResource toResource(SecuritySettings e) {
        if (e == null) return null;
        return new SecuritySettingsResource(e.getId(), e.getFarmId(), e.getMotionSensitivity(),
                e.getAlertMode() != null ? e.getAlertMode().name() : null,
                e.getDetectionScheduleStart(), e.getDetectionScheduleEnd(), e.getNotificationContacts(),
                e.getDisabledZoneIds());
    }
}
