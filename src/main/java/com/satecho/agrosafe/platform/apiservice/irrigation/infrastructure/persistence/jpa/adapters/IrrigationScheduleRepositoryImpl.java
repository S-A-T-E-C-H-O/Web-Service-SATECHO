package com.satecho.agrosafe.platform.apiservice.irrigation.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.irrigation.domain.model.aggregates.IrrigationSchedule;
import com.satecho.agrosafe.platform.irrigation.domain.repositories.IrrigationScheduleRepository;
import com.satecho.agrosafe.platform.irrigation.infrastructure.persistence.jpa.assemblers.IrrigationSchedulePersistenceAssembler;
import com.satecho.agrosafe.platform.irrigation.infrastructure.persistence.jpa.repositories.IrrigationSchedulePersistenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class IrrigationScheduleRepositoryImpl implements IrrigationScheduleRepository {

    private final IrrigationSchedulePersistenceRepository repository;

    @Override
    public IrrigationSchedule save(IrrigationSchedule schedule) {
        var entity = IrrigationSchedulePersistenceAssembler.toPersistenceFromDomain(schedule);
        var saved = repository.save(entity);
        return IrrigationSchedulePersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public void delete(IrrigationSchedule schedule) {
        var entity = IrrigationSchedulePersistenceAssembler.toPersistenceFromDomain(schedule);
        repository.delete(entity);
    }

    @Override
    public Optional<IrrigationSchedule> findByIdAndZoneId(Long id, Long zoneId) {
        return repository.findByIdAndZoneId(id, zoneId)
                .map(IrrigationSchedulePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<IrrigationSchedule> findByZoneIdOrderByCreatedAtDesc(Long zoneId) {
        return repository.findByZoneIdOrderByCreatedAtDesc(zoneId)
                .stream()
                .map(IrrigationSchedulePersistenceAssembler::toDomainFromPersistence)
                .collect(Collectors.toList());
    }
}
