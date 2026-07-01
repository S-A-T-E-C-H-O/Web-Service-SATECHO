package com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.entities.FieldVisitPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldVisitPersistenceRepository extends JpaRepository<FieldVisitPersistenceEntity, Long> {
    List<FieldVisitPersistenceEntity> findByAgronomistId(Long agronomistId);
}
