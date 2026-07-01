package com.satecho.agrosafe.platform.apiservice.subscriptions.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates.Subscription;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.repositories.SubscriptionRepository;
import com.satecho.agrosafe.platform.apiservice.subscriptions.infrastructure.persistence.jpa.assemblers.SubscriptionPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.subscriptions.infrastructure.persistence.jpa.repositories.SubscriptionPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SubscriptionRepositoryImpl implements SubscriptionRepository {

    private final SubscriptionPersistenceRepository subscriptionPersistenceRepository;

    public SubscriptionRepositoryImpl(SubscriptionPersistenceRepository subscriptionPersistenceRepository) {
        this.subscriptionPersistenceRepository = subscriptionPersistenceRepository;
    }

    @Override
    public Optional<Subscription> findById(Long id) {
        return subscriptionPersistenceRepository.findById(id).map(SubscriptionPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<Subscription> findByUserId(Long userId) {
        return subscriptionPersistenceRepository.findByUserId(userId).map(SubscriptionPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Subscription save(Subscription subscription) {
        var saved = subscriptionPersistenceRepository.save(SubscriptionPersistenceAssembler.toPersistenceFromDomain(subscription));
        return SubscriptionPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public boolean existsById(Long id) {
        return subscriptionPersistenceRepository.existsById(id);
    }
}
