package com.satecho.agrosafe.platform.apiservice.security.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.security.domain.model.commands.UpdateSecuritySettingsCommand;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.valueobjects.AlertMode;
import com.satecho.agrosafe.platform.apiservice.security.interfaces.rest.resources.UpdateSecuritySettingsResource;

public class UpdateSecuritySettingsCommandFromResourceAssembler {
    private UpdateSecuritySettingsCommandFromResourceAssembler() {}
    public static UpdateSecuritySettingsCommand toCommand(Long farmId, UpdateSecuritySettingsResource resource) {
        AlertMode alertMode = null;
        if (resource.alertMode() != null && !resource.alertMode().isBlank())
            alertMode = AlertMode.valueOf(resource.alertMode().toUpperCase());
        return new UpdateSecuritySettingsCommand(farmId, resource.motionSensitivity(), alertMode,
                resource.detectionScheduleStart(), resource.detectionScheduleEnd(), resource.notificationContacts());
    }
}
