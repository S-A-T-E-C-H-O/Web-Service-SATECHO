package com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.entities.RecommendationPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationPersistenceRepository extends JpaRepository<RecommendationPersistenceEntity, Long> {

    List<RecommendationPersistenceEntity> findByFarmId(Long farmId);

    List<RecommendationPersistenceEntity> findByFarmerId(Long farmerId);

    long countByAgronomistId(Long agronomistId);
}
