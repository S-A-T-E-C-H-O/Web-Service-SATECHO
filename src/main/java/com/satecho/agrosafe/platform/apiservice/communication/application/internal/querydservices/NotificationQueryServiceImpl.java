package com.satecho.agrosafe.platform.apiservice.communication.application.internal.querydservices;

import com.satecho.agrosafe.platform.apiservice.communication.application.querydservices.NotificationQueryService;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.aggregates.Notification;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.queries.GetNotificationsByUserIdQuery;
import com.satecho.agrosafe.platform.apiservice.communication.domain.repositories.NotificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class NotificationQueryServiceImpl implements NotificationQueryService {

    private final NotificationRepository notificationRepository;

    public NotificationQueryServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public List<Notification> handle(GetNotificationsByUserIdQuery query) {
        return notificationRepository.findByUserIdWithFilters(query.userId(), query.status(), query.type(), query.read(), query.limit());
    }

    @Override
    public Optional<Notification> getById(Long notificationId) {
        return notificationRepository.findById(notificationId);
    }
}