package com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Subscription;
import com.satecho.agrosafe.platform.apiservice.billing.domain.repositories.SubscriptionRepository;
import com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.assemblers.SubscriptionPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.repositories.SubscriptionPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA implementation adapter for the {@link SubscriptionRepository} port.
 * Translates queries and updates between domain aggregates and database persistence tables.
 */
@Repository
public class SubscriptionRepositoryImpl implements SubscriptionRepository {

    /**
     * Spring Data JPA repository for subscriptions.
     */
    private final SubscriptionPersistenceRepository persistenceRepository;

    /**
     * Constructs a new SubscriptionRepositoryImpl.
     *
     * @param persistenceRepository the underlying database repository client
     */
    public SubscriptionRepositoryImpl(SubscriptionPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    /**
     * Saves a subscription object, translating it between domain and persistence formats.
     *
     * @param subscription the subscription domain aggregate
     * @return the saved subscription domain aggregate
     */
    @Override
    public Subscription save(Subscription subscription) {
        var saved = persistenceRepository.save(SubscriptionPersistenceAssembler.toPersistenceFromDomain(subscription));
        return SubscriptionPersistenceAssembler.toDomainFromPersistence(saved);
    }

    /**
     * Finds a subscription by its user ID and transforms it into the domain representation.
     *
     * @param userId the ID of the subscriber
     * @return an {@link Optional} containing the subscription domain aggregate, or empty
     */
    @Override
    public Optional<Subscription> findByUserId(Long userId) {
        return persistenceRepository.findByUserId(userId).map(SubscriptionPersistenceAssembler::toDomainFromPersistence);
    }
}
