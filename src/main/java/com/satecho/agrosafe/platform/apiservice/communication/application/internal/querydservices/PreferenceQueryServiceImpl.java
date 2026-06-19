package com.satecho.agrosafe.platform.apiservice.communication.application.internal.querydservices;

import com.satecho.agrosafe.platform.apiservice.communication.application.querydservices.PreferenceQueryService;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.aggregates.NotificationPreference;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.queries.GetNotificationPreferencesByUserIdQuery;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.NotificationType;
import com.satecho.agrosafe.platform.apiservice.communication.domain.repositories.NotificationPreferenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PreferenceQueryServiceImpl implements PreferenceQueryService {

    private final NotificationPreferenceRepository preferenceRepository;

    public PreferenceQueryServiceImpl(NotificationPreferenceRepository preferenceRepository) {
        this.preferenceRepository = preferenceRepository;
    }

    @Override
    public List<NotificationPreference> handle(GetNotificationPreferencesByUserIdQuery query) {
        var preferences = preferenceRepository.findByUserId(query.userId());
        if (preferences.isEmpty()) {
            preferences = createDefaultPreferences(query.userId());
        }
        return preferences;
    }

    private List<NotificationPreference> createDefaultPreferences(Long userId) {
        List<NotificationPreference> defaults = new ArrayList<>();
        for (NotificationType type : NotificationType.values()) {
            defaults.add(preferenceRepository.save(NotificationPreference.createDefault(userId, type)));
        }
        return defaults;
    }
}