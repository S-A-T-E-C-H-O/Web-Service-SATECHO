package com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.AlertStatus;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.AlertType;
import com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.entities.AlertPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlertPersistenceRepository extends JpaRepository<AlertPersistenceEntity, Long> {
    Optional<AlertPersistenceEntity> findByZoneIdAndAlertTypeAndStatus(Long zoneId, AlertType alertType, AlertStatus status);
    List<AlertPersistenceEntity> findByZoneIdOrderByAlertCreatedAtDesc(Long zoneId);
    List<AlertPersistenceEntity> findByFarmIdAndStatusOrderByAlertCreatedAtDesc(Long farmId, AlertStatus status);
}
