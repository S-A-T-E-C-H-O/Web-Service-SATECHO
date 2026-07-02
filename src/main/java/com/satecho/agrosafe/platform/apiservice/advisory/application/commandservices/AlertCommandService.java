package com.satecho.agrosafe.platform.apiservice.advisory.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.soil.domain.model.events.TelemetryReceivedEvent;

public interface AlertCommandService {
    /**
     * Evaluates a soil telemetry reading against the owning zone's configured thresholds,
     * creating or resolving {@link com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.Alert}
     * instances with hysteresis (an active alert auto-resolves once the reading returns to the safe range).
     */
    void evaluateReading(TelemetryReceivedEvent event);
}
