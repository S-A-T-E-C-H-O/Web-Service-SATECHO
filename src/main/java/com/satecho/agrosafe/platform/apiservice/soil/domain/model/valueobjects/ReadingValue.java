package com.satecho.agrosafe.platform.apiservice.soil.domain.model.valueobjects;

public record ReadingValue(MetricType metricType, Double value, String unit) {
    public ReadingValue {
        if (metricType == null) throw new IllegalArgumentException("Metric type is required");
        if (value == null) throw new IllegalArgumentException("Reading value is required");
        if (!metricType.isValidValue(value))
            throw new IllegalArgumentException(String.format("Value %.2f is out of valid range [%.2f, %.2f] for %s",
                    value, metricType.getMinValid(), metricType.getMaxValid(), metricType.name()));
    }
    public static ReadingValue of(MetricType metricType, Double value) {
        return new ReadingValue(metricType, value, metricType.getUnit());
    }
}
