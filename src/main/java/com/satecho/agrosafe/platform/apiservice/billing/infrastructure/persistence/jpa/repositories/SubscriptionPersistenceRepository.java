package com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.entities.SubscriptionPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository client for the 'subscriptions' database table operations.
 */
@Repository
public interface SubscriptionPersistenceRepository extends JpaRepository<SubscriptionPersistenceEntity, Long> {
    
    /**
     * Finds a subscription entity matching the subscriber's user ID.
     *
     * @param userId the user identifier
     * @return an {@link Optional} containing the entity if found, or empty
     */
    Optional<SubscriptionPersistenceEntity> findByUserId(Long userId);
}
