package com.satecho.agrosafe.platform.apiservice.onboarding.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.onboarding.infrastructure.persistence.jpa.entities.IrrigationZonePersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ZonePersistenceRepository extends JpaRepository<IrrigationZonePersistenceEntity, Long> {
    List<IrrigationZonePersistenceEntity> findAllByFarmId(Long farmId);
    java.util.Optional<IrrigationZonePersistenceEntity> findByDeviceId(Long deviceId);
}