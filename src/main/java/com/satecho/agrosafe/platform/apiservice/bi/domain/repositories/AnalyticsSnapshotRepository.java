package com.satecho.agrosafe.platform.apiservice.bi.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.AnalyticsSnapshot;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.valueobjects.SnapshotPeriod;

import java.util.List;
import java.util.Optional;

public interface AnalyticsSnapshotRepository {
    List<AnalyticsSnapshot> findByPeriodOrderByFromDateDesc(SnapshotPeriod period);
    Optional<AnalyticsSnapshot> findTopByPeriodOrderByToDateDesc(SnapshotPeriod period);
    AnalyticsSnapshot save(AnalyticsSnapshot snapshot);
}
