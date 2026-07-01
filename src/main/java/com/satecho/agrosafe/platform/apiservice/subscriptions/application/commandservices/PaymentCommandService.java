package com.satecho.agrosafe.platform.apiservice.subscriptions.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates.Payment;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.commands.ProcessPaymentCommand;

public interface PaymentCommandService {
    Result<Payment, ApplicationError> processPayment(ProcessPaymentCommand command);
}
