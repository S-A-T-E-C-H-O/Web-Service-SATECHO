package com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.DiagnosticSession;
import com.satecho.agrosafe.platform.apiservice.bi.infrastructure.persistence.jpa.entities.DiagnosticSessionPersistenceEntity;

public final class DiagnosticSessionPersistenceAssembler {

    private DiagnosticSessionPersistenceAssembler() {
    }

    public static DiagnosticSession toDomainFromPersistence(DiagnosticSessionPersistenceEntity entity) {
        if (entity == null) return null;
        var domain = new DiagnosticSession();
        domain.setId(entity.getId());
        domain.setDeviceId(entity.getDeviceId());
        domain.setStatus(entity.getStatus());
        domain.setComponentResults(entity.getComponentResults());
        domain.setRecommendation(entity.getRecommendation());
        domain.setStartedAt(entity.getStartedAt());
        domain.setCompletedAt(entity.getCompletedAt());
        return domain;
    }

    public static DiagnosticSessionPersistenceEntity toPersistenceFromDomain(DiagnosticSession domain) {
        if (domain == null) return null;
        var entity = new DiagnosticSessionPersistenceEntity();
        if (domain.getId() != null) {
            entity.setId(domain.getId());
        }
        entity.setDeviceId(domain.getDeviceId());
        entity.setStatus(domain.getStatus());
        entity.setComponentResults(domain.getComponentResults());
        entity.setRecommendation(domain.getRecommendation());
        entity.setStartedAt(domain.getStartedAt());
        entity.setCompletedAt(domain.getCompletedAt());
        return entity;
    }
}
