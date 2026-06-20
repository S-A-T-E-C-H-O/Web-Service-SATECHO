package com.satecho.agrosafe.platform.apiservice.security.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.security.domain.model.aggregates.SecuritySettings;
import com.satecho.agrosafe.platform.apiservice.security.domain.repositories.SecuritySettingsRepository;
import com.satecho.agrosafe.platform.apiservice.security.infrastructure.persistence.jpa.assemblers.SecuritySettingsPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.security.infrastructure.persistence.jpa.repositories.SecuritySettingsPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SecuritySettingsRepositoryImpl implements SecuritySettingsRepository {

    private final SecuritySettingsPersistenceRepository persistenceRepository;

    public SecuritySettingsRepositoryImpl(SecuritySettingsPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public SecuritySettings save(SecuritySettings settings) {
        var saved = persistenceRepository.save(SecuritySettingsPersistenceAssembler.toPersistenceFromDomain(settings));
        return SecuritySettingsPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public Optional<SecuritySettings> findByFarmId(Long farmId) {
        return persistenceRepository.findByFarmId(farmId).map(SecuritySettingsPersistenceAssembler::toDomainFromPersistence);
    }
}
