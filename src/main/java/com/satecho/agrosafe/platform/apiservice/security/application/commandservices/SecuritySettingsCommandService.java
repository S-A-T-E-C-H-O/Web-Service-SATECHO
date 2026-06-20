package com.satecho.agrosafe.platform.apiservice.security.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.security.domain.model.aggregates.SecuritySettings;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.commands.UpdateSecuritySettingsCommand;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;

public interface SecuritySettingsCommandService {
    Result<SecuritySettings, ApplicationError> updateSettings(UpdateSecuritySettingsCommand command);
}
