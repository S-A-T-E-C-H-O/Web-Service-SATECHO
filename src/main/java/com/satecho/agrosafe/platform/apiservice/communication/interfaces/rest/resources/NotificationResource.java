package com.satecho.agrosafe.platform.apiservice.communication.interfaces.rest.resources;

import java.time.Instant;

public record NotificationResource(Long id, Long userId, String type, String title, String body,
                                   String channel, String deliveryStatus, Boolean read, Long relatedEntityId,
                                   String relatedEntityType, Instant sentAt, Instant deliveredAt, Instant readAt) {}