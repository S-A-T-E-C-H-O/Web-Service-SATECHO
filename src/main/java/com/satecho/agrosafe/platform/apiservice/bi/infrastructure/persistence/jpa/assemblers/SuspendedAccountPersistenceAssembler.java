package com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.SuspendedAccount;
import com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.entities.SuspendedAccountPersistenceEntity;

public final class SuspendedAccountPersistenceAssembler {

    private SuspendedAccountPersistenceAssembler() {
    }

    public static SuspendedAccount toDomainFromPersistence(SuspendedAccountPersistenceEntity entity) {
        if (entity == null) return null;
        var domain = new SuspendedAccount();
        domain.setId(entity.getId());
        domain.setUserId(entity.getUserId());
        domain.setReason(entity.getReason());
        domain.setSuspendedAt(entity.getSuspendedAt());
        domain.setReactivatedAt(entity.getReactivatedAt());
        domain.setSuspendedBy(entity.getSuspendedBy());
        return domain;
    }

    public static SuspendedAccountPersistenceEntity toPersistenceFromDomain(SuspendedAccount domain) {
        if (domain == null) return null;
        var entity = new SuspendedAccountPersistenceEntity();
        if (domain.getId() != null) {
            entity.setId(domain.getId());
        }
        entity.setUserId(domain.getUserId());
        entity.setReason(domain.getReason());
        entity.setSuspendedAt(domain.getSuspendedAt());
        entity.setReactivatedAt(domain.getReactivatedAt());
        entity.setSuspendedBy(domain.getSuspendedBy());
        return entity;
    }
}
