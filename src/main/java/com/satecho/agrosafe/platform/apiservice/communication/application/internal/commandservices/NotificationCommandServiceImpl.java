package com.satecho.agrosafe.platform.apiservice.communication.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.communication.application.commandservices.NotificationCommandService;
import com.satecho.agrosafe.platform.apiservice.communication.application.outbound.ports.NotificationDispatcherPort;
import com.satecho.agrosafe.platform.apiservice.communication.domain.exceptions.NotificationNotFoundException;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.aggregates.Notification;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.commands.SendNotificationCommand;
import com.satecho.agrosafe.platform.apiservice.communication.domain.repositories.NotificationRepository;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class NotificationCommandServiceImpl implements NotificationCommandService {


    private final NotificationRepository notificationRepository;
    private final NotificationDispatcherPort dispatcher;

    public NotificationCommandServiceImpl(NotificationRepository notificationRepository,
                                          NotificationDispatcherPort dispatcher) {
        this.notificationRepository = notificationRepository;
        this.dispatcher = dispatcher;
    }

    @Override
    public Result<Notification, ApplicationError> sendNotification(SendNotificationCommand command) {
        var notification = new Notification(command.userId(), command.type(), command.title(), command.body(),
                command.channel(), command.relatedEntityId(), command.relatedEntityType());
        notification.setRecipientAddress(command.recipientAddress());
        notification.dispatch();
        var saved = notificationRepository.save(notification);

        var dispatchResult = dispatcher.dispatch(saved);
        if (dispatchResult.isFailure()) {
            saved.markAsFailed();
            notificationRepository.save(saved);
        }
        return Result.success(saved);
    }

    @Override
    public Result<Notification, ApplicationError> markAsRead(Long notificationId) {
        var notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException(notificationId));
        notification.markAsRead();
        return Result.success(notificationRepository.save(notification));
    }
}