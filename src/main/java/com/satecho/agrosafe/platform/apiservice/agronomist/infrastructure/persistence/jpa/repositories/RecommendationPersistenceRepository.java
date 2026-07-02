package com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.entities.RecommendationPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecommendationPersistenceRepository extends JpaRepository<RecommendationPersistenceEntity, Long> {
    List<RecommendationPersistenceEntity> findByFarmerUserIdOrderByGeneratedAtDesc(Long farmerUserId);
    List<RecommendationPersistenceEntity> findByAgronomistUserIdOrderByGeneratedAtDesc(Long agronomistUserId);
}
