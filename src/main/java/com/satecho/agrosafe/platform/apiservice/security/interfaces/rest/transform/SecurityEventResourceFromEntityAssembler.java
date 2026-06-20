package com.satecho.agrosafe.platform.apiservice.security.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.security.domain.model.aggregates.SecurityEvent;
import com.satecho.agrosafe.platform.apiservice.security.interfaces.rest.resources.SecurityEventResource;
import java.util.List;

public class SecurityEventResourceFromEntityAssembler {
    private SecurityEventResourceFromEntityAssembler() {}
    public static SecurityEventResource toResource(SecurityEvent e) {
        if (e == null) return null;
        return new SecurityEventResource(e.getId(), e.getFarmId(), e.getDeviceId(),
                e.getClassification() != null ? e.getClassification().name() : null,
                e.getConfidenceLevel(), e.getSeverity() != null ? e.getSeverity().name() : null,
                e.getDetectedAt(), e.getAcknowledged(), e.getAcknowledgedBy(), e.getAcknowledgedAt(),
                e.getLocationDescription(), e.getRawData());
    }
    public static List<SecurityEventResource> toResourceList(List<SecurityEvent> entities) {
        if (entities == null) return List.of();
        return entities.stream().map(SecurityEventResourceFromEntityAssembler::toResource).toList();
    }
}
