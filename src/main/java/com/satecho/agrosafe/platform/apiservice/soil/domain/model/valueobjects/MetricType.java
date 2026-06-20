package com.satecho.agrosafe.platform.apiservice.soil.domain.model.valueobjects;

public enum MetricType {
    SOIL_MOISTURE("%", 0.0, 100.0),
    ELECTRICAL_CONDUCTIVITY("dS/m", 0.0, 20.0),
    SOIL_PH("pH", 0.0, 14.0),
    SOIL_TEMPERATURE("C", -10.0, 60.0),
    AMBIENT_TEMPERATURE("C", -10.0, 60.0),
    HUMIDITY("%", 0.0, 100.0);

    private final String unit;
    private final double minValid;
    private final double maxValid;

    MetricType(String unit, double minValid, double maxValid) {
        this.unit = unit;
        this.minValid = minValid;
        this.maxValid = maxValid;
    }

    public String getUnit() { return unit; }
    public double getMinValid() { return minValid; }
    public double getMaxValid() { return maxValid; }

    public boolean isValidValue(Double value) {
        if (value == null) return false;
        return value >= minValid && value <= maxValid;
    }
}
