package com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.valueobjects.SnapshotPeriod;
import com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.entities.AnalyticsSnapshotPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnalyticsSnapshotPersistenceRepository extends JpaRepository<AnalyticsSnapshotPersistenceEntity, Long> {

    List<AnalyticsSnapshotPersistenceEntity> findByPeriodOrderByFromDateDesc(SnapshotPeriod period);

    Optional<AnalyticsSnapshotPersistenceEntity> findTopByPeriodOrderByToDateDesc(SnapshotPeriod period);
}
