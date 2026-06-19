package com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.commands;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.valueobjects.ThresholdLimits;

public record UpdateZoneThresholdCommand(Long zoneId, ThresholdLimits thresholds) {
    public UpdateZoneThresholdCommand {
        if (zoneId == null) throw new IllegalArgumentException("Zone ID is required");
        if (thresholds == null) throw new IllegalArgumentException("Threshold limits are required");
    }
}