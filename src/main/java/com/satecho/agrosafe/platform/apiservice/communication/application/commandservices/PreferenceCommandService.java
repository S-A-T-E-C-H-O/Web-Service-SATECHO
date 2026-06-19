package com.satecho.agrosafe.platform.apiservice.communication.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.communication.domain.model.aggregates.NotificationPreference;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.commands.UpdateNotificationPreferencesCommand;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;

public interface PreferenceCommandService {
    Result<NotificationPreference, ApplicationError> updatePreferences(UpdateNotificationPreferencesCommand command);
}