package com.satecho.agrosafe.platform.apiservice.analytics.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.security.domain.model.aggregates.SecurityEvent;

import java.util.List;

public interface SecurityEventCsvExportService {
    /** Renders a CSV of the given farm's perimeter security events (EP-013-US003). */
    byte[] toCsv(List<SecurityEvent> events);
}
