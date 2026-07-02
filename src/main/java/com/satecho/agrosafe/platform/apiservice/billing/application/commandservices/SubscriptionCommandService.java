package com.satecho.agrosafe.platform.apiservice.billing.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Subscription;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.commands.SubscribeCommand;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;

public interface SubscriptionCommandService {
    Result<Subscription, ApplicationError> subscribe(SubscribeCommand command);
    Result<Subscription, ApplicationError> cancel(Long userId);
}
