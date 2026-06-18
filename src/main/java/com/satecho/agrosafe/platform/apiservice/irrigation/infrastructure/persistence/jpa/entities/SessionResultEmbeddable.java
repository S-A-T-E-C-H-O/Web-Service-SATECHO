package com.satecho.agrosafe.platform.apiservice.irrigation.infrastructure.persistence.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class SessionResultEmbeddable {
    @Column(name = "result_total_water_used_liters")
    private Double totalWaterUsedLiters;
    @Column(name = "result_water_saved_liters")
    private Double waterSavedLiters;
    @Column(name = "result_duration_minutes")
    private Integer durationMinutes;
}
