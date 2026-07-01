package com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.entities.DiagnosticSessionPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiagnosticSessionPersistenceRepository extends JpaRepository<DiagnosticSessionPersistenceEntity, Long> {

    List<DiagnosticSessionPersistenceEntity> findByDeviceIdOrderByStartedAtDesc(Long deviceId);
}
