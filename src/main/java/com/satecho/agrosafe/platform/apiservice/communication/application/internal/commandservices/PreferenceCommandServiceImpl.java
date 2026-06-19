package com.satecho.agrosafe.platform.apiservice.communication.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.communication.application.commandservices.PreferenceCommandService;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.aggregates.NotificationPreference;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.commands.UpdateNotificationPreferencesCommand;
import com.satecho.agrosafe.platform.apiservice.communication.domain.repositories.NotificationPreferenceRepository;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PreferenceCommandServiceImpl implements PreferenceCommandService {

    private final NotificationPreferenceRepository preferenceRepository;

    public PreferenceCommandServiceImpl(NotificationPreferenceRepository preferenceRepository) {
        this.preferenceRepository = preferenceRepository;
    }

    @Override
    public Result<NotificationPreference, ApplicationError> updatePreferences(UpdateNotificationPreferencesCommand command) {
        var existing = preferenceRepository.findByUserIdAndNotificationType(command.userId(), command.notificationType());
        NotificationPreference preference;
        if (existing.isPresent()) {
            preference = existing.get();
            preference.update(command.channelsEnabled(), command.dailyDigestEnabled(), command.quietHoursStart(), command.quietHoursEnd());
        } else {
            preference = new NotificationPreference(command.userId(), command.notificationType(), command.channelsEnabled(),
                    command.dailyDigestEnabled(), command.quietHoursStart(), command.quietHoursEnd());
        }
        return Result.success(preferenceRepository.save(preference));
    }
}