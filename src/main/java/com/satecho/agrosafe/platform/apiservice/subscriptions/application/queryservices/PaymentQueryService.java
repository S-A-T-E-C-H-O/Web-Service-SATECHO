package com.satecho.agrosafe.platform.apiservice.subscriptions.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentQueryService {
    List<Payment> findByUserId(Long userId);
    Optional<Payment> findByTransactionId(String transactionId);
}
