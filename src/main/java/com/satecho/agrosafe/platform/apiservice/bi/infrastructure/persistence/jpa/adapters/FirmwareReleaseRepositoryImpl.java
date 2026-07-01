package com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.FirmwareRelease;
import com.satecho.agrosafe.platform.apiservice.bi.domain.repositories.FirmwareReleaseRepository;
import com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.assemblers.FirmwareReleasePersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.repositories.FirmwareReleasePersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FirmwareReleaseRepositoryImpl implements FirmwareReleaseRepository {

    private final FirmwareReleasePersistenceRepository persistenceRepository;

    public FirmwareReleaseRepositoryImpl(FirmwareReleasePersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public Optional<FirmwareRelease> findById(Long id) {
        return persistenceRepository.findById(id)
                .map(FirmwareReleasePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<FirmwareRelease> findByDeviceModelOrderByReleasedAtDesc(String deviceModel) {
        return persistenceRepository.findByDeviceModelOrderByReleasedAtDesc(deviceModel).stream()
                .map(FirmwareReleasePersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Optional<FirmwareRelease> findByDeviceModelAndActiveTrue(String deviceModel) {
        return persistenceRepository.findByDeviceModelAndActiveTrue(deviceModel)
                .map(FirmwareReleasePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public FirmwareRelease save(FirmwareRelease release) {
        var saved = persistenceRepository.save(FirmwareReleasePersistenceAssembler.toPersistenceFromDomain(release));
        return FirmwareReleasePersistenceAssembler.toDomainFromPersistence(saved);
    }
}
