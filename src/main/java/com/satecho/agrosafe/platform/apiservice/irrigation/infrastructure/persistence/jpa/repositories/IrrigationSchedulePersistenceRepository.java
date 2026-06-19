package com.satecho.agrosafe.platform.apiservice.irrigation.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.irrigation.infrastructure.persistence.jpa.entities.IrrigationSchedulePersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IrrigationSchedulePersistenceRepository extends JpaRepository<IrrigationSchedulePersistenceEntity, Long> {
    List<IrrigationSchedulePersistenceEntity> findByZoneIdOrderByCreatedAtDesc(Long zoneId);
    Optional<IrrigationSchedulePersistenceEntity> findByIdAndZoneId(Long id, Long zoneId);
}
