package com.satecho.agrosafe.platform.apiservice.soil.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.soil.domain.model.valueobjects.MetricType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class ReadingValueEmbeddable {
    @Enumerated(EnumType.STRING)
    @Column(name = "metric_type", nullable = false, length = 30)
    private MetricType metricType;

    @Column(name = "reading_value", nullable = false)
    private Double value;

    @Column(name = "reading_unit", nullable = false, length = 10)
    private String unit;
}
