package com.satecho.agrosafe.platform.apiservice.security.domain.model.commands;

import com.satecho.agrosafe.platform.apiservice.security.domain.model.valueobjects.AlertMode;

public record UpdateSecuritySettingsCommand(Long farmId, Integer motionSensitivity, AlertMode alertMode,
                                            String detectionScheduleStart, String detectionScheduleEnd,
                                            String notificationContacts) {
    public UpdateSecuritySettingsCommand {
        if (farmId == null) throw new IllegalArgumentException("farmId cannot be null");
    }
}
