package com.satecho.agrosafe.platform.apiservice.security.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.security.domain.model.valueobjects.EventClassification;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.valueobjects.EventSeverity;
import com.satecho.agrosafe.platform.apiservice.security.infrastructure.persistence.jpa.entities.SecurityEventPersistenceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface SecurityEventPersistenceRepository extends JpaRepository<SecurityEventPersistenceEntity, Long>,
        org.springframework.data.jpa.repository.JpaSpecificationExecutor<SecurityEventPersistenceEntity> {
    @Query("SELECT se FROM SecurityEventPersistenceEntity se WHERE se.farmId = :farmId " +
            "AND (:from IS NULL OR se.detectedAt >= :from) AND (:to IS NULL OR se.detectedAt <= :to) " +
            "AND (:severity IS NULL OR se.severity = :severity) AND (:classification IS NULL OR se.classification = :classification) " +
            "ORDER BY se.detectedAt DESC")
    Page<SecurityEventPersistenceEntity> findByFarmIdWithFilters(
            @Param("farmId") Long farmId, @Param("from") Instant from, @Param("to") Instant to,
            @Param("severity") EventSeverity severity, @Param("classification") EventClassification classification,
            Pageable pageable);

    static Specification<SecurityEventPersistenceEntity> hasFarmId(Long farmId) {
        return (root, query, builder) -> builder.equal(root.get("farmId"), farmId);
    }

    static Specification<SecurityEventPersistenceEntity> detectedAtGreaterThanOrEqualTo(Instant from) {
        return (root, query, builder) -> from == null ? builder.conjunction() : builder.greaterThanOrEqualTo(root.get("detectedAt"), from);
    }

    static Specification<SecurityEventPersistenceEntity> detectedAtLessThanOrEqualTo(Instant to) {
        return (root, query, builder) -> to == null ? builder.conjunction() : builder.lessThanOrEqualTo(root.get("detectedAt"), to);
    }

    static Specification<SecurityEventPersistenceEntity> hasSeverity(EventSeverity severity) {
        return (root, query, builder) -> severity == null ? builder.conjunction() : builder.equal(root.get("severity"), severity);
    }

    static Specification<SecurityEventPersistenceEntity> hasClassification(EventClassification classification) {
        return (root, query, builder) -> classification == null ? builder.conjunction() : builder.equal(root.get("classification"), classification);
    }
}
