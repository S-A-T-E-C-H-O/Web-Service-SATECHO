package com.satecho.agrosafe.platform.apiservice.security.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.security.application.queryservices.SecuritySettingsQueryService;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.aggregates.SecuritySettings;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.queries.GetSecuritySettingsByFarmQuery;
import com.satecho.agrosafe.platform.apiservice.security.domain.repositories.SecuritySettingsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SecuritySettingsQueryServiceImpl implements SecuritySettingsQueryService {

    private final SecuritySettingsRepository securitySettingsRepository;

    public SecuritySettingsQueryServiceImpl(SecuritySettingsRepository securitySettingsRepository) {
        this.securitySettingsRepository = securitySettingsRepository;
    }

    @Override
    public Optional<SecuritySettings> handle(GetSecuritySettingsByFarmQuery query) {
        return securitySettingsRepository.findByFarmId(query.farmId());
    }
}
