package com.satecho.agrosafe.platform.soil.domain;

import com.satecho.agrosafe.platform.soil.domain.model.aggregates.SensorReading;
import com.satecho.agrosafe.platform.soil.domain.model.valueobjects.MetricType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SensorReading")
class SensorReadingTest {

    private static final Long DEVICE_ID = 1L;
    private static final Long ZONE_ID = 2L;
    private static final Instant NOW = Instant.now();

    @Test
    @DisplayName("no-arg constructor creates instance without throwing")
    void noArgConstructor_createsInstance() {
        SensorReading reading = new SensorReading();
        assertThat(reading).isNotNull();
        assertThat(reading.getReadingValue()).isNull();
    }

    @Test
    @DisplayName("full constructor sets isValid=true for in-range value")
    void constructor_inRangeValue_setsIsValidTrue() {
        SensorReading reading = new SensorReading(DEVICE_ID, ZONE_ID, MetricType.SOIL_MOISTURE, 50.0, NOW);
        assertThat(reading.getIsValid()).isTrue();
    }

    @Test
    @DisplayName("full constructor sets deviceId and zoneId correctly")
    void constructor_setsDeviceIdAndZoneId() {
        SensorReading reading = new SensorReading(DEVICE_ID, ZONE_ID, MetricType.SOIL_PH, 6.5, NOW);
        assertThat(reading.getDeviceId()).isEqualTo(DEVICE_ID);
        assertThat(reading.getZoneId()).isEqualTo(ZONE_ID);
    }

    @Test
    @DisplayName("full constructor sets timestamp to provided value")
    void constructor_setsTimestamp() {
        SensorReading reading = new SensorReading(DEVICE_ID, ZONE_ID, MetricType.HUMIDITY, 70.0, NOW);
        assertThat(reading.getTimestamp()).isEqualTo(NOW);
    }

    @Test
    @DisplayName("full constructor sets ingestedAt to a non-null Instant")
    void constructor_setsIngestedAt() {
        SensorReading reading = new SensorReading(DEVICE_ID, ZONE_ID, MetricType.HUMIDITY, 70.0, NOW);
        assertThat(reading.getIngestedAt()).isNotNull();
    }

    @Test
    @DisplayName("getValue() delegates to ReadingValue.value()")
    void getValue_delegatesToReadingValue() {
        SensorReading reading = new SensorReading(DEVICE_ID, ZONE_ID, MetricType.SOIL_MOISTURE, 42.5, NOW);
        assertThat(reading.getValue()).isEqualTo(42.5);
    }

    @Test
    @DisplayName("getMetricType() delegates to ReadingValue.metricType()")
    void getMetricType_delegatesToReadingValue() {
        SensorReading reading = new SensorReading(DEVICE_ID, ZONE_ID, MetricType.ELECTRICAL_CONDUCTIVITY, 5.0, NOW);
        assertThat(reading.getMetricType()).isEqualTo(MetricType.ELECTRICAL_CONDUCTIVITY);
    }

    @Test
    @DisplayName("getUnit() returns the unit string from MetricType")
    void getUnit_returnsMetricTypeUnit() {
        SensorReading reading = new SensorReading(DEVICE_ID, ZONE_ID, MetricType.SOIL_PH, 7.0, NOW);
        assertThat(reading.getUnit()).isEqualTo("pH");
    }

    @Test
    @DisplayName("getValue() returns null when readingValue is null")
    void getValue_nullReadingValue_returnsNull() {
        SensorReading reading = new SensorReading();
        assertThat(reading.getValue()).isNull();
    }

    @Test
    @DisplayName("getMetricType() returns null when readingValue is null")
    void getMetricType_nullReadingValue_returnsNull() {
        SensorReading reading = new SensorReading();
        assertThat(reading.getMetricType()).isNull();
    }

    @Test
    @DisplayName("getUnit() returns null when readingValue is null")
    void getUnit_nullReadingValue_returnsNull() {
        SensorReading reading = new SensorReading();
        assertThat(reading.getUnit()).isNull();
    }

    @Test
    @DisplayName("setters update fields correctly")
    void setters_updateFields() {
        SensorReading reading = new SensorReading();
        reading.setId(99L);
        reading.setDeviceId(10L);
        reading.setZoneId(20L);
        assertThat(reading.getId()).isEqualTo(99L);
        assertThat(reading.getDeviceId()).isEqualTo(10L);
        assertThat(reading.getZoneId()).isEqualTo(20L);
    }
}
