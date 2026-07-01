package com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.Recommendation;
import com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.entities.RecommendationPersistenceEntity;

public final class RecommendationPersistenceAssembler {
    private RecommendationPersistenceAssembler() {}

    public static Recommendation toDomainFromPersistence(RecommendationPersistenceEntity e) {
        if (e == null) return null;
        var d = new Recommendation();
        d.setId(e.getId());
        d.setFarmId(e.getFarmId());
        d.setZoneId(e.getZoneId());
        d.setAgronomistId(e.getAgronomistId());
        d.setFarmerId(e.getFarmerId());
        d.setType(e.getType());
        d.setPriority(e.getPriority());
        d.setStatus(e.getStatus());
        d.setTitle(e.getTitle());
        d.setDescription(e.getDescription());
        d.setRecommendedActions(e.getRecommendedActions());
        d.setGeneratedAt(e.getGeneratedAt());
        d.setSentAt(e.getSentAt());
        d.setAcknowledgedAt(e.getAcknowledgedAt());
        d.setAcknowledgedBy(e.getAcknowledgedBy());
        return d;
    }

    public static RecommendationPersistenceEntity toPersistenceFromDomain(Recommendation d) {
        if (d == null) return null;
        var e = new RecommendationPersistenceEntity();
        if (d.getId() != null) e.setId(d.getId());
        e.setFarmId(d.getFarmId());
        e.setZoneId(d.getZoneId());
        e.setAgronomistId(d.getAgronomistId());
        e.setFarmerId(d.getFarmerId());
        e.setType(d.getType());
        e.setPriority(d.getPriority());
        e.setStatus(d.getStatus());
        e.setTitle(d.getTitle());
        e.setDescription(d.getDescription());
        e.setRecommendedActions(d.getRecommendedActions());
        e.setGeneratedAt(d.getGeneratedAt());
        e.setSentAt(d.getSentAt());
        e.setAcknowledgedAt(d.getAcknowledgedAt());
        e.setAcknowledgedBy(d.getAcknowledgedBy());
        return e;
    }
}
