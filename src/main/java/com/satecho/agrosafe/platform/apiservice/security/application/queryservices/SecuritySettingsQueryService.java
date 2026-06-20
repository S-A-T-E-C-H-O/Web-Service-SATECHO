package com.satecho.agrosafe.platform.apiservice.security.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.security.domain.model.aggregates.SecuritySettings;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.queries.GetSecuritySettingsByFarmQuery;

import java.util.Optional;

public interface SecuritySettingsQueryService {
    Optional<SecuritySettings> handle(GetSecuritySettingsByFarmQuery query);
}
