package com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Subscription;
import com.satecho.agrosafe.platform.apiservice.billing.domain.repositories.SubscriptionRepository;
import com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.assemblers.SubscriptionPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.repositories.SubscriptionPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SubscriptionRepositoryImpl implements SubscriptionRepository {

    private final SubscriptionPersistenceRepository persistenceRepository;

    public SubscriptionRepositoryImpl(SubscriptionPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public Subscription save(Subscription subscription) {
        var saved = persistenceRepository.save(SubscriptionPersistenceAssembler.toPersistenceFromDomain(subscription));
        return SubscriptionPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public Optional<Subscription> findByUserId(Long userId) {
        return persistenceRepository.findByUserId(userId).map(SubscriptionPersistenceAssembler::toDomainFromPersistence);
    }
}
