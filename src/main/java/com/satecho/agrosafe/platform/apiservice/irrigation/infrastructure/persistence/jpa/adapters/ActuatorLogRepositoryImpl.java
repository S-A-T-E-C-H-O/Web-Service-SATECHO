package com.satecho.agrosafe.platform.apiservice.irrigation.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.aggregates.ActuatorLog;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.repositories.ActuatorLogRepository;
import com.satecho.agrosafe.platform.apiservice.irrigation.infrastructure.persistence.jpa.assemblers.ActuatorLogPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.irrigation.infrastructure.persistence.jpa.repositories.ActuatorLogPersistenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ActuatorLogRepositoryImpl implements ActuatorLogRepository {

    private final ActuatorLogPersistenceRepository repository;

    @Override
    public ActuatorLog save(ActuatorLog log) {
        var entity = ActuatorLogPersistenceAssembler.toPersistenceFromDomain(log);
        var saved = repository.save(entity);
        return ActuatorLogPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public List<ActuatorLog> findByDeviceIdOrderByExecutedAtDesc(Long deviceId, int limit) {
        return repository.findByDeviceIdOrderByExecutedAtDesc(deviceId, PageRequest.of(0, limit))
                .stream()
                .map(ActuatorLogPersistenceAssembler::toDomainFromPersistence)
                .collect(Collectors.toList());
    }

    @Override
    public List<ActuatorLog> findByDeviceIdAndExecutedAtBetween(Long deviceId, Instant from, Instant to, int limit) {
        return repository.findByDeviceIdAndExecutedAtBetween(deviceId, from, to, PageRequest.of(0, limit))
                .stream()
                .map(ActuatorLogPersistenceAssembler::toDomainFromPersistence)
                .collect(Collectors.toList());
    }
}
