package com.satecho.agrosafe.platform.apiservice.subscriptions.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates.Subscription;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.commands.CancelSubscriptionCommand;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.commands.CreateSubscriptionCommand;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.commands.UpdatePlanCommand;

public interface SubscriptionCommandService {
    Result<Subscription, ApplicationError> createSubscription(CreateSubscriptionCommand command);
    Result<Subscription, ApplicationError> createTrialSubscription(Long userId);
    Result<Subscription, ApplicationError> updatePlan(Long subscriptionId, UpdatePlanCommand command);
    Result<Subscription, ApplicationError> cancelSubscription(CancelSubscriptionCommand command);
}
