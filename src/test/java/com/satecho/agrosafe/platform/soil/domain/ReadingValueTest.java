package com.satecho.agrosafe.platform.soil.domain;

import com.satecho.agrosafe.platform.soil.domain.model.valueobjects.MetricType;
import com.satecho.agrosafe.platform.soil.domain.model.valueobjects.ReadingValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ReadingValue")
class ReadingValueTest {

    @Test
    @DisplayName("of() constructs ReadingValue with correct metricType, value, and unit")
    void of_validArgs_setsFields() {
        ReadingValue rv = ReadingValue.of(MetricType.SOIL_MOISTURE, 55.0);
        assertThat(rv.metricType()).isEqualTo(MetricType.SOIL_MOISTURE);
        assertThat(rv.value()).isEqualTo(55.0);
        assertThat(rv.unit()).isEqualTo("%");
    }

    @Test
    @DisplayName("compact constructor accepts valid min boundary value")
    void constructor_atMinBoundary_isValid() {
        ReadingValue rv = new ReadingValue(MetricType.SOIL_PH, 0.0, "pH");
        assertThat(rv.value()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("compact constructor accepts valid max boundary value")
    void constructor_atMaxBoundary_isValid() {
        ReadingValue rv = new ReadingValue(MetricType.SOIL_PH, 14.0, "pH");
        assertThat(rv.value()).isEqualTo(14.0);
    }

    @Test
    @DisplayName("compact constructor throws IllegalArgumentException for null metricType")
    void constructor_nullMetricType_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> new ReadingValue(null, 50.0, "%"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Metric type is required");
    }

    @Test
    @DisplayName("compact constructor throws IllegalArgumentException for null value")
    void constructor_nullValue_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> new ReadingValue(MetricType.SOIL_MOISTURE, null, "%"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Reading value is required");
    }

    @Test
    @DisplayName("compact constructor throws IllegalArgumentException when value is above max")
    void constructor_valueAboveMax_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> new ReadingValue(MetricType.SOIL_MOISTURE, 100.001, "%"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("out of valid range");
    }

    @Test
    @DisplayName("compact constructor throws IllegalArgumentException when value is below min")
    void constructor_valueBelowMin_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> new ReadingValue(MetricType.SOIL_TEMPERATURE, -10.001, "C"))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("out of valid range");
    }

    @Test
    @DisplayName("of() throws IllegalArgumentException for out-of-range value")
    void of_outOfRange_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> ReadingValue.of(MetricType.ELECTRICAL_CONDUCTIVITY, 21.0))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("of() unit is derived from MetricType.getUnit()")
    void of_unit_matchesMetricTypeUnit() {
        assertThat(ReadingValue.of(MetricType.ELECTRICAL_CONDUCTIVITY, 5.0).unit())
            .isEqualTo(MetricType.ELECTRICAL_CONDUCTIVITY.getUnit());
    }

    @Test
    @DisplayName("ReadingValue is a record with structural equality")
    void recordEquality() {
        ReadingValue a = ReadingValue.of(MetricType.SOIL_PH, 7.0);
        ReadingValue b = ReadingValue.of(MetricType.SOIL_PH, 7.0);
        assertThat(a).isEqualTo(b);
    }
}
