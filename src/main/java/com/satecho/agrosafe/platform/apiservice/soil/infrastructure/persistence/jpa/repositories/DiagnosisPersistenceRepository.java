package com.satecho.agrosafe.platform.apiservice.soil.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.soil.infrastructure.persistence.jpa.entities.DiagnosisPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiagnosisPersistenceRepository extends JpaRepository<DiagnosisPersistenceEntity, Long> {
    Optional<DiagnosisPersistenceEntity> findTopByZoneIdOrderByGeneratedAtDesc(Long zoneId);
}
