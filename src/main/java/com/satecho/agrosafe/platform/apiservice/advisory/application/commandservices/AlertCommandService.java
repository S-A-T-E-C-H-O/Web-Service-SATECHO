package com.satecho.agrosafe.platform.apiservice.advisory.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.soil.domain.model.events.TelemetryReceivedEvent;

public interface AlertCommandService {

    void evaluateReading(TelemetryReceivedEvent event);
}
