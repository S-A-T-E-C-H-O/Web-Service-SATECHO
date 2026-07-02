package com.satecho.agrosafe.platform.apiservice.billing.domain.model.commands;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects.PlanTier;

public record SubscribeCommand(Long userId, PlanTier planTier) {
}
