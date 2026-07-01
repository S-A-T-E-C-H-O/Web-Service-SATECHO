package com.satecho.agrosafe.platform.apiservice.advisory.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.advisory.application.queryservices.AlertQueryService;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.Alert;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.AlertStatus;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.repositories.AlertRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertQueryServiceImpl implements AlertQueryService {

    private final AlertRepository alertRepository;

    public AlertQueryServiceImpl(AlertRepository alertRepository) {
        this.alertRepository = alertRepository;
    }

    @Override
    public List<Alert> findByZoneId(Long zoneId) {
        return alertRepository.findByZoneIdOrderByCreatedAtDesc(zoneId);
    }

    @Override
    public List<Alert> findActiveByFarmId(Long farmId) {
        return alertRepository.findByFarmIdAndStatusOrderByCreatedAtDesc(farmId, AlertStatus.ACTIVE);
    }
}