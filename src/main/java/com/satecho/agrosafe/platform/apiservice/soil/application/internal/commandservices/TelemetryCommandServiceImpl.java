package com.satecho.agrosafe.platform.apiservice.soil.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.soil.application.commandservices.TelemetryCommandService;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.aggregates.SensorReading;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.commands.BatchIngestCommand;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.commands.IngestTelemetryCommand;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.events.TelemetryReceivedEvent;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.events.ThresholdBreachedEvent;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.valueobjects.MetricType;
import com.satecho.agrosafe.platform.apiservice.soil.domain.repositories.SensorReadingRepository;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
public class TelemetryCommandServiceImpl implements TelemetryCommandService {

    private final SensorReadingRepository sensorReadingRepository;

    public TelemetryCommandServiceImpl(SensorReadingRepository sensorReadingRepository) {
        this.sensorReadingRepository = sensorReadingRepository;
    }

    @Override
    public Result<SensorReading, ApplicationError> ingestTelemetry(IngestTelemetryCommand command) {
        if (!command.metricType().isValidValue(command.value())) {
            return Result.failure(ApplicationError.validationError("reading",
                    String.format("Value %.2f is out of valid range [%.2f, %.2f] for %s",
                            command.value(), command.metricType().getMinValid(),
                            command.metricType().getMaxValid(), command.metricType().name())));
        }
        Instant timestamp = command.timestamp() != null ? command.timestamp() : Instant.now();
        var reading = new SensorReading(command.deviceId(), command.zoneId(), command.metricType(), command.value(), timestamp);
        var saved = sensorReadingRepository.save(reading);
        saved.addDomainEvent(new TelemetryReceivedEvent(saved.getId(), saved.getDeviceId(), saved.getZoneId(),
                saved.getMetricType().name(), saved.getValue(), saved.getTimestamp()));
        checkThresholds(saved);
        return Result.success(saved);
    }

    @Override
    public Result<Integer, ApplicationError> batchIngest(BatchIngestCommand command) {
        int ingested = 0;
        for (var reading : command.readings()) {
            var result = ingestTelemetry(reading);
            if (result instanceof Result.Failure<SensorReading, ApplicationError> f) return Result.failure(f.error());
            ingested++;
        }
        return Result.success(ingested);
    }

    private void checkThresholds(SensorReading reading) {
        MetricType metricType = reading.getMetricType();
        Double value = reading.getValue();
        double range = metricType.getMaxValid() - metricType.getMinValid();
        double minThreshold = metricType.getMinValid() + range * 0.1;
        double maxThreshold = metricType.getMaxValid() - range * 0.1;
        if (value < minThreshold || value > maxThreshold) {
            reading.addDomainEvent(new ThresholdBreachedEvent(reading.getId(), reading.getDeviceId(),
                    reading.getZoneId(), metricType.name(), value, minThreshold, maxThreshold));
        }
    }
}
