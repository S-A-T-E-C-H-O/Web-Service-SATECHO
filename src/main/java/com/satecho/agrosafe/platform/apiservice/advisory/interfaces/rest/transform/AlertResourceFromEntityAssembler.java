package com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.Alert;
import com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.resources.AlertResource;

import java.util.List;

public final class AlertResourceFromEntityAssembler {
    private AlertResourceFromEntityAssembler() {}

    public static AlertResource toResource(Alert a) {
        return new AlertResource(a.getId(), a.getZoneId(), a.getDeviceId(), a.getFarmId(),
                a.getAlertType().name(), a.getSeverity().name(), a.getValue(), a.getThresholdValue(),
                a.getStatus().name(), a.getCreatedAt(), a.getResolvedAt());
    }

    public static List<AlertResource> toResourceList(List<Alert> alerts) {
        return alerts.stream().map(AlertResourceFromEntityAssembler::toResource).toList();
    }
}
