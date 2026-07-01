package com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.AnalyticsSnapshot;
import com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.entities.AnalyticsSnapshotPersistenceEntity;

public final class AnalyticsSnapshotPersistenceAssembler {

    private AnalyticsSnapshotPersistenceAssembler() {
    }

    public static AnalyticsSnapshot toDomainFromPersistence(AnalyticsSnapshotPersistenceEntity entity) {
        if (entity == null) return null;
        var domain = new AnalyticsSnapshot();
        domain.setId(entity.getId());
        domain.setPeriod(entity.getPeriod());
        domain.setFromDate(entity.getFromDate());
        domain.setToDate(entity.getToDate());
        domain.setActiveUsers(entity.getActiveUsers());
        domain.setMrr(entity.getMrr());
        domain.setCurrency(entity.getCurrency());
        domain.setConversionRate(entity.getConversionRate());
        domain.setChurnRate(entity.getChurnRate());
        domain.setNewSubscriptions(entity.getNewSubscriptions());
        domain.setCanceledSubscriptions(entity.getCanceledSubscriptions());
        domain.setRawData(entity.getRawData());
        return domain;
    }

    public static AnalyticsSnapshotPersistenceEntity toPersistenceFromDomain(AnalyticsSnapshot domain) {
        if (domain == null) return null;
        var entity = new AnalyticsSnapshotPersistenceEntity();
        if (domain.getId() != null) {
            entity.setId(domain.getId());
        }
        entity.setPeriod(domain.getPeriod());
        entity.setFromDate(domain.getFromDate());
        entity.setToDate(domain.getToDate());
        entity.setActiveUsers(domain.getActiveUsers());
        entity.setMrr(domain.getMrr());
        entity.setCurrency(domain.getCurrency());
        entity.setConversionRate(domain.getConversionRate());
        entity.setChurnRate(domain.getChurnRate());
        entity.setNewSubscriptions(domain.getNewSubscriptions());
        entity.setCanceledSubscriptions(domain.getCanceledSubscriptions());
        entity.setRawData(domain.getRawData());
        return entity;
    }
}
