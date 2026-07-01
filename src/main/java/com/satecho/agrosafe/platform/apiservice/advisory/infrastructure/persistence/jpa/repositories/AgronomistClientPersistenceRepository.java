package com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.entities.AgronomistClientPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgronomistClientPersistenceRepository extends JpaRepository<AgronomistClientPersistenceEntity, Long> {

    List<AgronomistClientPersistenceEntity> findByAgronomistIdAndActiveTrue(Long agronomistId);

    Optional<AgronomistClientPersistenceEntity> findByFarmerIdAndActiveTrue(Long farmerId);

    Optional<AgronomistClientPersistenceEntity> findByAgronomistIdAndFarmerIdAndActiveTrue(Long agronomistId, Long farmerId);

    long countByAgronomistId(Long agronomistId);

    long countByAgronomistIdAndActiveTrue(Long agronomistId);
}
