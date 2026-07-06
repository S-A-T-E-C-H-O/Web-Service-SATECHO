package com.satecho.agrosafe.platform.apiservice.subscriptions.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.subscriptions.infrastructure.persistence.jpa.entities.InvoicePersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("subscriptionsInvoicePersistenceRepository")
public interface InvoicePersistenceRepository extends JpaRepository<InvoicePersistenceEntity, Long> {
    List<InvoicePersistenceEntity> findByUserId(Long userId);
    List<InvoicePersistenceEntity> findBySubscriptionId(Long subscriptionId);
}
