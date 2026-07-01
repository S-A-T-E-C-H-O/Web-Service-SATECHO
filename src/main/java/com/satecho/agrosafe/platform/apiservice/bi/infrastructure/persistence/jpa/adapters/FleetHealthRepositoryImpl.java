package com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.FleetHealth;
import com.satecho.agrosafe.platform.apiservice.bi.domain.repositories.FleetHealthRepository;
import com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.assemblers.FleetHealthPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.repositories.FleetHealthPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class FleetHealthRepositoryImpl implements FleetHealthRepository {

    private final FleetHealthPersistenceRepository persistenceRepository;

    public FleetHealthRepositoryImpl(FleetHealthPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public Optional<FleetHealth> findTopByOrderBySnapshotAtDesc() {
        return persistenceRepository.findTopByOrderBySnapshotAtDesc()
                .map(FleetHealthPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public FleetHealth save(FleetHealth fleetHealth) {
        var saved = persistenceRepository.save(FleetHealthPersistenceAssembler.toPersistenceFromDomain(fleetHealth));
        return FleetHealthPersistenceAssembler.toDomainFromPersistence(saved);
    }
}
