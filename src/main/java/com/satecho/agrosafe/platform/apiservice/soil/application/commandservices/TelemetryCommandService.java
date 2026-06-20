package com.satecho.agrosafe.platform.apiservice.soil.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.soil.domain.model.aggregates.SensorReading;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.commands.BatchIngestCommand;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.commands.IngestTelemetryCommand;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;

public interface TelemetryCommandService {
    Result<SensorReading, ApplicationError> ingestTelemetry(IngestTelemetryCommand command);
    Result<Integer, ApplicationError> batchIngest(BatchIngestCommand command);
}
