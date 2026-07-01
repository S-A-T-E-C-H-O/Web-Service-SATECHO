package com.satecho.agrosafe.platform.apiservice.subscriptions.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.subscriptions.infrastructure.persistence.jpa.entities.PaymentPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentPersistenceRepository extends JpaRepository<PaymentPersistenceEntity, Long> {
    List<PaymentPersistenceEntity> findByUserId(Long userId);
    List<PaymentPersistenceEntity> findBySubscriptionId(Long subscriptionId);
    Optional<PaymentPersistenceEntity> findByTransactionId(String transactionId);
}
