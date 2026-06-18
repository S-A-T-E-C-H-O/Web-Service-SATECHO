package com.satecho.agrosafe.platform.apiservice.irrigation.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.irrigation.domain.model.valueobjects.IrrigationStatus;
import com.satecho.agrosafe.platform.irrigation.infrastructure.persistence.jpa.entities.IrrigationSessionPersistenceEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface IrrigationSessionPersistenceRepository extends JpaRepository<IrrigationSessionPersistenceEntity, Long> {
    Optional<IrrigationSessionPersistenceEntity> findByZoneIdAndStatus(Long zoneId, IrrigationStatus status);
    @Query("SELECT s FROM IrrigationSessionPersistenceEntity s WHERE s.zoneId = :zoneId AND s.status = 'ACTIVE'")
    Optional<IrrigationSessionPersistenceEntity> findActiveByZoneId(@Param("zoneId") Long zoneId);
    List<IrrigationSessionPersistenceEntity> findByZoneIdOrderByStartedAtDesc(Long zoneId, Pageable pageable);
    @Query("SELECT s FROM IrrigationSessionPersistenceEntity s WHERE s.zoneId = :zoneId AND s.startedAt >= :from AND s.startedAt <= :to ORDER BY s.startedAt DESC")
    List<IrrigationSessionPersistenceEntity> findByZoneIdAndStartedAtBetween(@Param("zoneId") Long zoneId, @Param("from") Instant from, @Param("to") Instant to, Pageable pageable);
}
