package com.satecho.agrosafe.platform.apiservice.advisory.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.Alert;

import java.util.List;

public interface AlertQueryService {
    List<Alert> findByZoneId(Long zoneId);
    List<Alert> findActiveByFarmId(Long farmId);
}