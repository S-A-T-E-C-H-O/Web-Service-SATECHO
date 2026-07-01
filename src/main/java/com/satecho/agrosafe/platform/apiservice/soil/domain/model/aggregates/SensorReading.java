package com.satecho.agrosafe.platform.apiservice.soil.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.soil.domain.model.valueobjects.MetricType;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.valueobjects.ReadingValue;
import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
public class SensorReading extends AuditableAbstractAggregateRoot<SensorReading> {
    @Setter private Long id;
    private Long deviceId;
    private Long zoneId;
    private ReadingValue readingValue;
    private Instant timestamp;
    private Instant ingestedAt;
    private Boolean isValid;

    public SensorReading() {}

    public SensorReading(Long deviceId, Long zoneId, MetricType metricType, Double value, Instant timestamp) {
        this.deviceId = deviceId;
        this.zoneId = zoneId;
        this.readingValue = ReadingValue.of(metricType, value);
        this.timestamp = timestamp;
        this.ingestedAt = Instant.now();
        this.isValid = metricType.isValidValue(value);
    }

    public Double getValue() { return readingValue != null ? readingValue.value() : null; }
    public MetricType getMetricType() { return readingValue != null ? readingValue.metricType() : null; }
    public String getUnit() { return readingValue != null ? readingValue.unit() : null; }
}
