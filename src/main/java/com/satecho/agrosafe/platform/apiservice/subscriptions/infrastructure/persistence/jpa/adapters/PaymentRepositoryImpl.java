package com.satecho.agrosafe.platform.apiservice.subscriptions.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates.Payment;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.repositories.PaymentRepository;
import com.satecho.agrosafe.platform.apiservice.subscriptions.infrastructure.persistence.jpa.assemblers.PaymentPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.subscriptions.infrastructure.persistence.jpa.repositories.PaymentPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentPersistenceRepository paymentPersistenceRepository;

    public PaymentRepositoryImpl(PaymentPersistenceRepository paymentPersistenceRepository) {
        this.paymentPersistenceRepository = paymentPersistenceRepository;
    }

    @Override
    public Optional<Payment> findById(Long id) {
        return paymentPersistenceRepository.findById(id).map(PaymentPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<Payment> findByUserId(Long userId) {
        return paymentPersistenceRepository.findByUserId(userId).stream()
                .map(PaymentPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<Payment> findBySubscriptionId(Long subscriptionId) {
        return paymentPersistenceRepository.findBySubscriptionId(subscriptionId).stream()
                .map(PaymentPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Optional<Payment> findByTransactionId(String transactionId) {
        return paymentPersistenceRepository.findByTransactionId(transactionId).map(PaymentPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Payment save(Payment payment) {
        var saved = paymentPersistenceRepository.save(PaymentPersistenceAssembler.toPersistenceFromDomain(payment));
        return PaymentPersistenceAssembler.toDomainFromPersistence(saved);
    }
}
