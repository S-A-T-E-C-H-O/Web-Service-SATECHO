package com.satecho.agrosafe.platform.apiservice.security.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.security.domain.model.valueobjects.EventClassification;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.valueobjects.EventSeverity;
import com.satecho.agrosafe.platform.apiservice.security.infrastructure.persistence.jpa.entities.SecurityEventPersistenceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface SecurityEventPersistenceRepository extends JpaRepository<SecurityEventPersistenceEntity, Long> {
    @Query("SELECT se FROM SecurityEventPersistenceEntity se WHERE se.farmId = :farmId " +
            "AND (:from IS NULL OR se.detectedAt >= :from) AND (:to IS NULL OR se.detectedAt <= :to) " +
            "AND (:severity IS NULL OR se.severity = :severity) AND (:classification IS NULL OR se.classification = :classification) " +
            "ORDER BY se.detectedAt DESC")
    Page<SecurityEventPersistenceEntity> findByFarmIdWithFilters(
            @Param("farmId") Long farmId, @Param("from") Instant from, @Param("to") Instant to,
            @Param("severity") EventSeverity severity, @Param("classification") EventClassification classification,
            Pageable pageable);
}
