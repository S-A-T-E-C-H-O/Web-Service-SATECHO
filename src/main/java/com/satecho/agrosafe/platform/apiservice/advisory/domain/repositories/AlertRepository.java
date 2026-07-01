package com.satecho.agrosafe.platform.apiservice.advisory.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.Alert;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.AlertStatus;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.AlertType;

import java.util.List;
import java.util.Optional;

public interface AlertRepository {
    Alert save(Alert alert);
    Optional<Alert> findById(Long id);
    Optional<Alert> findByZoneIdAndAlertTypeAndStatus(Long zoneId, AlertType alertType, AlertStatus status);
    List<Alert> findByZoneIdOrderByCreatedAtDesc(Long zoneId);
    List<Alert> findByFarmIdAndStatusOrderByCreatedAtDesc(Long farmId, AlertStatus status);
}