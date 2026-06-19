package com.satecho.agrosafe.platform.apiservice.communication.infrastructure.outbound.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.aggregates.Notification;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FirebasePushAdapter {

    private static final Logger log = LoggerFactory.getLogger(FirebasePushAdapter.class);
    private final FirebaseMessaging firebaseMessaging;

    public FirebasePushAdapter(@Autowired(required = false) FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    public Result<Void, ApplicationError> send(Notification notification) {
        if (firebaseMessaging == null) {
            return Result.failure(ApplicationError.unexpected("FirebasePush", "Firebase not configured (FIREBASE_ENABLED=false)"));
        }
        if (notification.getRecipientAddress() == null) {
            return Result.failure(ApplicationError.validationError("recipientAddress", "Device FCM token required for PUSH channel"));
        }
        try {
            var message = Message.builder()
                    .setToken(notification.getRecipientAddress())
                    .setNotification(com.google.firebase.messaging.Notification.builder()
                            .setTitle(notification.getTitle())
                            .setBody(notification.getBody())
                            .build())
                    .build();
            firebaseMessaging.send(message);
            return Result.success(null);
        } catch (FirebaseMessagingException e) {
            log.error("Firebase push failed for userId={}: {}", notification.getUserId(), e.getMessage());
            return Result.failure(ApplicationError.unexpected("FirebasePush", e.getMessage()));
        }
    }
}