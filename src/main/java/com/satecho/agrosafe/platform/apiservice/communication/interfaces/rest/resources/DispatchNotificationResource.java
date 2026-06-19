package com.satecho.agrosafe.platform.apiservice.communication.interfaces.rest.resources;

public record DispatchNotificationResource(Long userId, String type, String title, String body,
                                           String channel, Long relatedEntityId, String relatedEntityType,
                                           String recipientAddress) {}