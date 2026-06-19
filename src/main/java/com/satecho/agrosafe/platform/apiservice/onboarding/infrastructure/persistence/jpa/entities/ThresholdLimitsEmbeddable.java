package com.satecho.agrosafe.platform.apiservice.onboarding.infrastructure.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class ThresholdLimitsEmbeddable {

    @Column(name = "min_moisture", nullable = false)
    private double minMoisture;

    @Column(name = "max_moisture", nullable = false)
    private double maxMoisture;

    @Column(name = "min_ec", nullable = false)
    private double minEc;

    @Column(name = "max_ec", nullable = false)
    private double maxEc;

    @Column(name = "min_ph", nullable = false)
    private double minPh;

    @Column(name = "max_ph", nullable = false)
    private double maxPh;

    @Column(name = "min_temperature", nullable = false)
    private double minTemperature;

    @Column(name = "max_temperature", nullable = false)
    private double maxTemperature;
}