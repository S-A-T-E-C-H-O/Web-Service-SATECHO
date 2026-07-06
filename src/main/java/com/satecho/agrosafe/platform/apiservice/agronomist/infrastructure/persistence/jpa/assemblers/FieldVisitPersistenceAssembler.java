package com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.FieldVisit;
import com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.entities.FieldVisitPersistenceEntity;

/**
 * Assembler class that converts between {@link FieldVisit} domain aggregates
 * and {@link FieldVisitPersistenceEntity} database entities.
 * <p>
 * This class consists exclusively of static methods to perform the mapping.
 * </p>
 */
public final class FieldVisitPersistenceAssembler {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private FieldVisitPersistenceAssembler() {}

    /**
     * Converts a {@link FieldVisitPersistenceEntity} to a {@link FieldVisit} domain aggregate.
     *
     * @param e the persistence entity to convert
     * @return the domain aggregate, or {@code null} if the input is null
     */
    public static FieldVisit toDomainFromPersistence(FieldVisitPersistenceEntity e) {
        if (e == null) return null;
        var d = new FieldVisit();
        d.setId(e.getId());
        d.setAgronomistUserId(e.getAgronomistUserId());
        d.setFarmId(e.getFarmId());
        d.setScheduledAt(e.getScheduledAt());
        d.setTag(e.getTag());
        d.setNoteTitle(e.getNoteTitle());
        d.setNoteBody(e.getNoteBody());
        d.setUrgent(e.getUrgent());
        d.setCompleted(e.getCompleted());
        d.setCompletedAt(e.getCompletedAt());
        d.setLatitude(e.getLatitude());
        d.setLongitude(e.getLongitude());
        d.setPhotoBase64(e.getPhotoBase64());
        return d;
    }

    /**
     * Converts a {@link FieldVisit} domain aggregate to a {@link FieldVisitPersistenceEntity}.
     *
     * @param d the domain aggregate to convert
     * @return the persistence entity, or {@code null} if the input is null
     */
    public static FieldVisitPersistenceEntity toPersistenceFromDomain(FieldVisit d) {
        if (d == null) return null;
        var e = new FieldVisitPersistenceEntity();
        if (d.getId() != null) e.setId(d.getId());
        e.setAgronomistUserId(d.getAgronomistUserId());
        e.setFarmId(d.getFarmId());
        e.setScheduledAt(d.getScheduledAt());
        e.setTag(d.getTag());
        e.setNoteTitle(d.getNoteTitle());
        e.setNoteBody(d.getNoteBody());
        e.setUrgent(d.getUrgent());
        e.setCompleted(d.getCompleted());
        e.setCompletedAt(d.getCompletedAt());
        e.setLatitude(d.getLatitude());
        e.setLongitude(d.getLongitude());
        e.setPhotoBase64(d.getPhotoBase64());
        return e;
    }
}
