package com.satecho.agrosafe.platform.apiservice.communication.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.communication.domain.model.aggregates.Notification;
import com.satecho.agrosafe.platform.apiservice.communication.interfaces.rest.resources.NotificationResource;

import java.util.List;

public class NotificationResourceFromEntityAssembler {
    private NotificationResourceFromEntityAssembler() {}
    public static NotificationResource toResource(Notification e) {
        if (e == null) return null;
        return new NotificationResource(e.getId(), e.getUserId(),
                e.getType() != null ? e.getType().name() : null, e.getTitle(), e.getBody(),
                e.getChannel() != null ? e.getChannel().name() : null,
                e.getDeliveryStatus() != null ? e.getDeliveryStatus().name() : null,
                e.getReadAt() != null,
                e.getRelatedEntityId(), e.getRelatedEntityType(),
                e.getSentAt(), e.getDeliveredAt(), e.getReadAt());
    }
    public static List<NotificationResource> toResourceList(List<Notification> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(NotificationResourceFromEntityAssembler::toResource).toList();
    }
}