package com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.entities.SubscriptionPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("billingSubscriptionPersistenceRepository")
public interface SubscriptionPersistenceRepository extends JpaRepository<SubscriptionPersistenceEntity, Long> {
    Optional<SubscriptionPersistenceEntity> findByUserId(Long userId);
}
