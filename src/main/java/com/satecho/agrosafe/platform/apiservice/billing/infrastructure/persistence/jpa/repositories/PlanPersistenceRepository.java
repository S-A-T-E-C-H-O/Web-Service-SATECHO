package com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects.PlanTier;
import com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.entities.PlanPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlanPersistenceRepository extends JpaRepository<PlanPersistenceEntity, Long> {
    Optional<PlanPersistenceEntity> findByTier(PlanTier tier);
    boolean existsByTier(PlanTier tier);
}
