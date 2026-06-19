package com.satecho.agrosafe.platform.apiservice.communication.application.querydservices;

import com.satecho.agrosafe.platform.apiservice.communication.domain.model.aggregates.NotificationPreference;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.queries.GetNotificationPreferencesByUserIdQuery;

import java.util.List;

public interface PreferenceQueryService {
    List<NotificationPreference> handle(GetNotificationPreferencesByUserIdQuery query);
}