package com.satecho.agrosafe.platform.apiservice.billing.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Subscription;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.commands.SubscribeCommand;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;

/**
 * Service interface for handling subscription change and cancellation commands.
 */
public interface SubscriptionCommandService {
    /**
     * Subscribes a user to a plan.
     *
     * @param command the command containing user ID and desired plan tier
     * @return a {@link Result} containing the created or updated {@link Subscription} or an {@link ApplicationError}
     */
    Result<Subscription, ApplicationError> subscribe(SubscribeCommand command);

    /**
     * Cancels a user's subscription.
     *
     * @param userId the ID of the user whose subscription is to be cancelled
     * @return a {@link Result} containing the cancelled {@link Subscription} or an {@link ApplicationError}
     */
    Result<Subscription, ApplicationError> cancel(Long userId);
}
