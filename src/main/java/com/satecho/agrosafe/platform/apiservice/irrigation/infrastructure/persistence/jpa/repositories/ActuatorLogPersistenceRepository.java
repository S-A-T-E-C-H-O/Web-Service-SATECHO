package com.satecho.agrosafe.platform.apiservice.irrigation.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.irrigation.infrastructure.persistence.jpa.entities.ActuatorLogPersistenceEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ActuatorLogPersistenceRepository extends JpaRepository<ActuatorLogPersistenceEntity, Long> {
    List<ActuatorLogPersistenceEntity> findByDeviceIdOrderByExecutedAtDesc(Long deviceId, Pageable pageable);
    @Query("SELECT a FROM ActuatorLogPersistenceEntity a WHERE a.deviceId = :deviceId AND a.executedAt >= :from AND a.executedAt <= :to ORDER BY a.executedAt DESC")
    List<ActuatorLogPersistenceEntity> findByDeviceIdAndExecutedAtBetween(@Param("deviceId") Long deviceId, @Param("from") Instant from, @Param("to") Instant to, Pageable pageable);
}
