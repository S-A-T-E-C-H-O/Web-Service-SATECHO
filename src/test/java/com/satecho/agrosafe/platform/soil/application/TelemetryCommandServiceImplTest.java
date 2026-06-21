package com.satecho.agrosafe.platform.soil.application;

import com.satecho.agrosafe.platform.apiservice.soil.application.internal.commandservices.TelemetryCommandServiceImpl;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.aggregates.SensorReading;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.commands.BatchIngestCommand;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.commands.IngestTelemetryCommand;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.valueobjects.MetricType;
import com.satecho.agrosafe.platform.apiservice.soil.domain.repositories.SensorReadingRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("TelemetryCommandServiceImpl")
class TelemetryCommandServiceImplTest {

    @Mock SensorReadingRepository sensorReadingRepository;
    @InjectMocks TelemetryCommandServiceImpl service;

    private SensorReading savedReading(Long deviceId, Long zoneId, MetricType type, double value) {
        SensorReading r = new SensorReading(deviceId, zoneId, type, value, Instant.now());
        r.setId(1L);
        return r;
    }

    // ── ingestTelemetry ───────────────────────────────────────────────────────

    @Test
    @DisplayName("ingestTelemetry: valid command saves reading and returns success")
    void ingestTelemetry_valid_returnsSuccess() {
        IngestTelemetryCommand cmd = new IngestTelemetryCommand(1L, 2L, MetricType.SOIL_MOISTURE, 55.0, Instant.now());
        SensorReading saved = savedReading(1L, 2L, MetricType.SOIL_MOISTURE, 55.0);
        when(sensorReadingRepository.save(any(SensorReading.class))).thenReturn(saved);

        var result = service.ingestTelemetry(cmd);

        assertThat(result.isSuccess()).isTrue();
        verify(sensorReadingRepository).save(any(SensorReading.class));
    }

    @Test
    @DisplayName("ingestTelemetry: null timestamp uses current time")
    void ingestTelemetry_nullTimestamp_usesCurrentTime() {
        IngestTelemetryCommand cmd = new IngestTelemetryCommand(1L, 2L, MetricType.SOIL_PH, 7.0, null);
        SensorReading saved = savedReading(1L, 2L, MetricType.SOIL_PH, 7.0);
        when(sensorReadingRepository.save(any(SensorReading.class))).thenReturn(saved);

        var result = service.ingestTelemetry(cmd);

        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    @DisplayName("ingestTelemetry: out-of-range value returns failure without saving")
    void ingestTelemetry_outOfRangeValue_returnsFailureWithoutSave() {
        IngestTelemetryCommand cmd = new IngestTelemetryCommand(1L, 2L, MetricType.SOIL_MOISTURE, 150.0, Instant.now());

        var result = service.ingestTelemetry(cmd);

        assertThat(result.isFailure()).isTrue();
        verifyNoInteractions(sensorReadingRepository);
    }

    @Test
    @DisplayName("ingestTelemetry: value at minimum boundary is accepted")
    void ingestTelemetry_atMinBoundary_returnsSuccess() {
        IngestTelemetryCommand cmd = new IngestTelemetryCommand(1L, 2L, MetricType.SOIL_TEMPERATURE, -10.0, Instant.now());
        SensorReading saved = savedReading(1L, 2L, MetricType.SOIL_TEMPERATURE, -10.0);
        when(sensorReadingRepository.save(any(SensorReading.class))).thenReturn(saved);

        var result = service.ingestTelemetry(cmd);

        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    @DisplayName("ingestTelemetry: value at maximum boundary is accepted")
    void ingestTelemetry_atMaxBoundary_returnsSuccess() {
        IngestTelemetryCommand cmd = new IngestTelemetryCommand(1L, 2L, MetricType.ELECTRICAL_CONDUCTIVITY, 20.0, Instant.now());
        SensorReading saved = savedReading(1L, 2L, MetricType.ELECTRICAL_CONDUCTIVITY, 20.0);
        when(sensorReadingRepository.save(any(SensorReading.class))).thenReturn(saved);

        var result = service.ingestTelemetry(cmd);

        assertThat(result.isSuccess()).isTrue();
    }

    @Test
    @DisplayName("ingestTelemetry: returned SensorReading has correct metricType and value")
    void ingestTelemetry_returnedReading_hasCorrectFields() {
        IngestTelemetryCommand cmd = new IngestTelemetryCommand(3L, 4L, MetricType.HUMIDITY, 80.0, Instant.now());
        SensorReading saved = savedReading(3L, 4L, MetricType.HUMIDITY, 80.0);
        when(sensorReadingRepository.save(any(SensorReading.class))).thenReturn(saved);

        var result = service.ingestTelemetry(cmd);

        assertThat(result.isSuccess()).isTrue();
        SensorReading reading = result.toOptional().get();
        assertThat(reading.getMetricType()).isEqualTo(MetricType.HUMIDITY);
        assertThat(reading.getValue()).isEqualTo(80.0);
    }

    // ── batchIngest ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("batchIngest: all valid readings returns success with ingested count")
    void batchIngest_allValid_returnsSuccessWithCount() {
        IngestTelemetryCommand cmd1 = new IngestTelemetryCommand(1L, 1L, MetricType.SOIL_MOISTURE, 40.0, Instant.now());
        IngestTelemetryCommand cmd2 = new IngestTelemetryCommand(1L, 1L, MetricType.SOIL_PH, 6.5, Instant.now());
        BatchIngestCommand batch = new BatchIngestCommand(List.of(cmd1, cmd2));

        when(sensorReadingRepository.save(any(SensorReading.class)))
            .thenAnswer(inv -> { SensorReading r = inv.getArgument(0); r.setId(1L); return r; });

        var result = service.batchIngest(batch);

        assertThat(result.isSuccess()).isTrue();
        Integer count = result.toOptional().get();
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("batchIngest: one out-of-range reading causes batch failure")
    void batchIngest_oneInvalid_returnsFailure() {
        IngestTelemetryCommand valid = new IngestTelemetryCommand(1L, 1L, MetricType.SOIL_MOISTURE, 40.0, Instant.now());
        IngestTelemetryCommand invalid = new IngestTelemetryCommand(1L, 1L, MetricType.SOIL_MOISTURE, 999.0, Instant.now());
        BatchIngestCommand batch = new BatchIngestCommand(List.of(valid, invalid));

        when(sensorReadingRepository.save(any(SensorReading.class)))
            .thenAnswer(inv -> { SensorReading r = inv.getArgument(0); r.setId(1L); return r; });

        var result = service.batchIngest(batch);

        assertThat(result.isFailure()).isTrue();
    }
}
