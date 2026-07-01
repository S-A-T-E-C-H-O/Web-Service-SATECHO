package com.satecho.agrosafe.platform.apiservice.subscriptions.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates.Payment;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository {
    Optional<Payment> findById(Long id);
    List<Payment> findByUserId(Long userId);
    List<Payment> findBySubscriptionId(Long subscriptionId);
    Optional<Payment> findByTransactionId(String transactionId);
    Payment save(Payment payment);
}
