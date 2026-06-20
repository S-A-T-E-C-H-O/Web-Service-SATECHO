package com.satecho.agrosafe.platform.apiservice.security.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.security.domain.model.aggregates.SecuritySettings;

import java.util.Optional;

public interface SecuritySettingsRepository {
    SecuritySettings save(SecuritySettings settings);
    Optional<SecuritySettings> findByFarmId(Long farmId);
}
