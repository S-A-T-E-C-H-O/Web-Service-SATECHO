package com.satecho.agrosafe.platform.soil.domain;

import com.satecho.agrosafe.platform.apiservice.soil.domain.model.valueobjects.MetricType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("MetricType")
class MetricTypeTest {

    @Test
    @DisplayName("isValidValue returns false for null on every metric type")
    void isValidValue_null_returnsFalse() {
        for (MetricType type : MetricType.values()) {
            assertThat(type.isValidValue(null)).isFalse();
        }
    }

    @Test @DisplayName("SOIL_MOISTURE: 0.0 (min) is valid")
    void soilMoisture_atMin_isValid() { assertThat(MetricType.SOIL_MOISTURE.isValidValue(0.0)).isTrue(); }

    @Test @DisplayName("SOIL_MOISTURE: 100.0 (max) is valid")
    void soilMoisture_atMax_isValid() { assertThat(MetricType.SOIL_MOISTURE.isValidValue(100.0)).isTrue(); }

    @Test @DisplayName("SOIL_MOISTURE: 50.0 (mid) is valid")
    void soilMoisture_mid_isValid() { assertThat(MetricType.SOIL_MOISTURE.isValidValue(50.0)).isTrue(); }

    @Test @DisplayName("SOIL_MOISTURE: -0.001 (just below min) is invalid")
    void soilMoisture_justBelowMin_isInvalid() { assertThat(MetricType.SOIL_MOISTURE.isValidValue(-0.001)).isFalse(); }

    @Test @DisplayName("SOIL_MOISTURE: 100.001 (just above max) is invalid")
    void soilMoisture_justAboveMax_isInvalid() { assertThat(MetricType.SOIL_MOISTURE.isValidValue(100.001)).isFalse(); }

    @Test @DisplayName("ELECTRICAL_CONDUCTIVITY: 0.0 (min) is valid")
    void ec_atMin_isValid() { assertThat(MetricType.ELECTRICAL_CONDUCTIVITY.isValidValue(0.0)).isTrue(); }

    @Test @DisplayName("ELECTRICAL_CONDUCTIVITY: 20.0 (max) is valid")
    void ec_atMax_isValid() { assertThat(MetricType.ELECTRICAL_CONDUCTIVITY.isValidValue(20.0)).isTrue(); }

    @Test @DisplayName("ELECTRICAL_CONDUCTIVITY: 20.001 (just above max) is invalid")
    void ec_justAboveMax_isInvalid() { assertThat(MetricType.ELECTRICAL_CONDUCTIVITY.isValidValue(20.001)).isFalse(); }

    @Test @DisplayName("ELECTRICAL_CONDUCTIVITY: -0.001 (just below min) is invalid")
    void ec_justBelowMin_isInvalid() { assertThat(MetricType.ELECTRICAL_CONDUCTIVITY.isValidValue(-0.001)).isFalse(); }

    @Test @DisplayName("SOIL_PH: 0.0 (min) is valid")
    void soilPh_atMin_isValid() { assertThat(MetricType.SOIL_PH.isValidValue(0.0)).isTrue(); }

    @Test @DisplayName("SOIL_PH: 14.0 (max) is valid")
    void soilPh_atMax_isValid() { assertThat(MetricType.SOIL_PH.isValidValue(14.0)).isTrue(); }

    @Test @DisplayName("SOIL_PH: 7.0 (neutral) is valid")
    void soilPh_neutral_isValid() { assertThat(MetricType.SOIL_PH.isValidValue(7.0)).isTrue(); }

    @Test @DisplayName("SOIL_PH: 14.001 (just above max) is invalid")
    void soilPh_justAboveMax_isInvalid() { assertThat(MetricType.SOIL_PH.isValidValue(14.001)).isFalse(); }

    @Test @DisplayName("SOIL_PH: -0.001 (just below min) is invalid")
    void soilPh_justBelowMin_isInvalid() { assertThat(MetricType.SOIL_PH.isValidValue(-0.001)).isFalse(); }

    @Test @DisplayName("SOIL_TEMPERATURE: -10.0 (min) is valid")
    void soilTemp_atMin_isValid() { assertThat(MetricType.SOIL_TEMPERATURE.isValidValue(-10.0)).isTrue(); }

    @Test @DisplayName("SOIL_TEMPERATURE: 60.0 (max) is valid")
    void soilTemp_atMax_isValid() { assertThat(MetricType.SOIL_TEMPERATURE.isValidValue(60.0)).isTrue(); }

    @Test @DisplayName("SOIL_TEMPERATURE: -10.001 (just below min) is invalid")
    void soilTemp_justBelowMin_isInvalid() { assertThat(MetricType.SOIL_TEMPERATURE.isValidValue(-10.001)).isFalse(); }

    @Test @DisplayName("SOIL_TEMPERATURE: 60.001 (just above max) is invalid")
    void soilTemp_justAboveMax_isInvalid() { assertThat(MetricType.SOIL_TEMPERATURE.isValidValue(60.001)).isFalse(); }

    @Test @DisplayName("AMBIENT_TEMPERATURE: 25.0 is valid")
    void ambientTemp_mid_isValid() { assertThat(MetricType.AMBIENT_TEMPERATURE.isValidValue(25.0)).isTrue(); }

    @Test @DisplayName("AMBIENT_TEMPERATURE: -10.001 (just below min) is invalid")
    void ambientTemp_justBelowMin_isInvalid() { assertThat(MetricType.AMBIENT_TEMPERATURE.isValidValue(-10.001)).isFalse(); }

    @Test @DisplayName("HUMIDITY: 100.0 (max) is valid")
    void humidity_atMax_isValid() { assertThat(MetricType.HUMIDITY.isValidValue(100.0)).isTrue(); }

    @Test @DisplayName("HUMIDITY: -1.0 is invalid")
    void humidity_negative_isInvalid() { assertThat(MetricType.HUMIDITY.isValidValue(-1.0)).isFalse(); }

    @Test
    @DisplayName("getUnit returns correct unit for every MetricType")
    void getUnit_allTypes() {
        assertThat(MetricType.SOIL_MOISTURE.getUnit()).isEqualTo("%");
        assertThat(MetricType.ELECTRICAL_CONDUCTIVITY.getUnit()).isEqualTo("dS/m");
        assertThat(MetricType.SOIL_PH.getUnit()).isEqualTo("pH");
        assertThat(MetricType.SOIL_TEMPERATURE.getUnit()).isEqualTo("C");
        assertThat(MetricType.AMBIENT_TEMPERATURE.getUnit()).isEqualTo("C");
        assertThat(MetricType.HUMIDITY.getUnit()).isEqualTo("%");
    }

    @ParameterizedTest(name = "{0}: min={1}, max={2}")
    @CsvSource({
        "SOIL_MOISTURE, 0.0, 100.0",
        "ELECTRICAL_CONDUCTIVITY, 0.0, 20.0",
        "SOIL_PH, 0.0, 14.0",
        "SOIL_TEMPERATURE, -10.0, 60.0",
        "AMBIENT_TEMPERATURE, -10.0, 60.0",
        "HUMIDITY, 0.0, 100.0"
    })
    @DisplayName("getMinValid and getMaxValid match enum definition")
    void getMinMaxValid_allTypes(String name, double expectedMin, double expectedMax) {
        MetricType type = MetricType.valueOf(name);
        assertThat(type.getMinValid()).isEqualTo(expectedMin);
        assertThat(type.getMaxValid()).isEqualTo(expectedMax);
    }
}
