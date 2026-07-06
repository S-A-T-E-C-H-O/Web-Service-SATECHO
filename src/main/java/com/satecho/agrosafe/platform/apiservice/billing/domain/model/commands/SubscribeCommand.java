package com.satecho.agrosafe.platform.apiservice.billing.domain.model.commands;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects.PlanTier;

/**
 * Command representing a request to subscribe a user to a specific plan tier.
 *
 * @param userId the ID of the user requesting subscription
 * @param planTier the desired {@link PlanTier}
 */
public record SubscribeCommand(Long userId, PlanTier planTier) {
}
