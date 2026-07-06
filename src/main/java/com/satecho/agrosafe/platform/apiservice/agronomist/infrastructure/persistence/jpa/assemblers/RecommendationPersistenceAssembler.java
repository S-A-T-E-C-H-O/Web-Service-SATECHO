package com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.Recommendation;
import com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.entities.RecommendationPersistenceEntity;

/**
 * Assembler class that converts between {@link Recommendation} domain aggregates
 * and {@link RecommendationPersistenceEntity} database entities.
 * <p>
 * This class consists exclusively of static methods to perform the mapping.
 * </p>
 */
public final class RecommendationPersistenceAssembler {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private RecommendationPersistenceAssembler() {}

    /**
     * Converts a {@link RecommendationPersistenceEntity} to a {@link Recommendation} domain aggregate.
     *
     * @param e the persistence entity to convert
     * @return the domain aggregate, or {@code null} if the input is null
     */
    public static Recommendation toDomainFromPersistence(RecommendationPersistenceEntity e) {
        if (e == null) return null;
        var d = new Recommendation();
        d.setId(e.getId());
        d.setAgronomistUserId(e.getAgronomistUserId());
        d.setFarmerUserId(e.getFarmerUserId());
        d.setZoneId(e.getZoneId());
        d.setTitle(e.getTitle());
        d.setDescription(e.getDescription());
        d.setRecommendedActions(e.getRecommendedActions());
        d.setAttachmentUrl(e.getAttachmentUrl());
        d.setPriority(e.getPriority());
        d.setStatus(e.getStatus());
        d.setGeneratedAt(e.getGeneratedAt());
        d.setResolvedAt(e.getResolvedAt());
        return d;
    }

    /**
     * Converts a {@link Recommendation} domain aggregate to a {@link RecommendationPersistenceEntity}.
     *
     * @param d the domain aggregate to convert
     * @return the persistence entity, or {@code null} if the input is null
     */
    public static RecommendationPersistenceEntity toPersistenceFromDomain(Recommendation d) {
        if (d == null) return null;
        var e = new RecommendationPersistenceEntity();
        if (d.getId() != null) e.setId(d.getId());
        e.setAgronomistUserId(d.getAgronomistUserId());
        e.setFarmerUserId(d.getFarmerUserId());
        e.setZoneId(d.getZoneId());
        e.setTitle(d.getTitle());
        e.setDescription(d.getDescription());
        e.setRecommendedActions(d.getRecommendedActions());
        e.setAttachmentUrl(d.getAttachmentUrl());
        e.setPriority(d.getPriority());
        e.setStatus(d.getStatus());
        e.setGeneratedAt(d.getGeneratedAt());
        e.setResolvedAt(d.getResolvedAt());
        return e;
    }
}
