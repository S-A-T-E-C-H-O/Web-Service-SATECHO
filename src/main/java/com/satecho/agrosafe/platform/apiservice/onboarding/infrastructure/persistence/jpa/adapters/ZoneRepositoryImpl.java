package com.satecho.agrosafe.platform.apiservice.onboarding.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.IrrigationZone;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.repositories.ZoneRepository;
import com.satecho.agrosafe.platform.apiservice.onboarding.infrastructure.persistence.jpa.assemblers.ZonePersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.onboarding.infrastructure.persistence.jpa.repositories.ZonePersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ZoneRepositoryImpl implements ZoneRepository {

    private final ZonePersistenceRepository zonePersistenceRepository;

    public ZoneRepositoryImpl(ZonePersistenceRepository zonePersistenceRepository) {
        this.zonePersistenceRepository = zonePersistenceRepository;
    }

    @Override
    public Optional<IrrigationZone> findById(Long id) {
        return zonePersistenceRepository.findById(id).map(ZonePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<IrrigationZone> findAllByFarmId(Long farmId) {
        return zonePersistenceRepository.findAllByFarmId(farmId).stream()
                .map(ZonePersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public IrrigationZone save(IrrigationZone zone) {
        var saved = zonePersistenceRepository.save(ZonePersistenceAssembler.toPersistenceFromDomain(zone));
        return ZonePersistenceAssembler.toDomainFromPersistence(saved);
    }
}