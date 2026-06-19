package com.satecho.agrosafe.platform.apiservice.onboarding.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "onboarding_progress")
@Getter
@Setter
@NoArgsConstructor
public class OnboardingProgressPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    @Column(nullable = false)
    private boolean completed;

    @Column(name = "current_step", nullable = false)
    private int currentStep;

    @Column(name = "farm_data_complete", nullable = false)
    private boolean farmDataComplete;

    @Column(name = "zone_config_complete", nullable = false)
    private boolean zoneConfigComplete;

    @Column(name = "device_linked_complete", nullable = false)
    private boolean deviceLinkedComplete;

    @Column(name = "thresholds_configured_complete", nullable = false)
    private boolean thresholdsConfiguredComplete;

    @Column(name = "completed_at")
    private Instant completedAt;
}