package com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.Alert;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.AlertStatus;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.AlertType;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.repositories.AlertRepository;
import com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.assemblers.AlertPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.repositories.AlertPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AlertRepositoryImpl implements AlertRepository {

    private final AlertPersistenceRepository persistenceRepository;

    public AlertRepositoryImpl(AlertPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public Alert save(Alert alert) {
        var saved = persistenceRepository.save(AlertPersistenceAssembler.toPersistenceFromDomain(alert));
        return AlertPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public Optional<Alert> findById(Long id) {
        return persistenceRepository.findById(id).map(AlertPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<Alert> findByZoneIdAndAlertTypeAndStatus(Long zoneId, AlertType alertType, AlertStatus status) {
        return persistenceRepository.findByZoneIdAndAlertTypeAndStatus(zoneId, alertType, status)
                .map(AlertPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<Alert> findByZoneIdOrderByCreatedAtDesc(Long zoneId) {
        return persistenceRepository.findByZoneIdOrderByAlertCreatedAtDesc(zoneId).stream()
                .map(AlertPersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public List<Alert> findByFarmIdAndStatusOrderByCreatedAtDesc(Long farmId, AlertStatus status) {
        return persistenceRepository.findByFarmIdAndStatusOrderByAlertCreatedAtDesc(farmId, status).stream()
                .map(AlertPersistenceAssembler::toDomainFromPersistence).toList();
    }
}
