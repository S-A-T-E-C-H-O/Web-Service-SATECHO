package com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.FieldVisit;
import com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.entities.FieldVisitPersistenceEntity;

public final class FieldVisitPersistenceAssembler {
    private FieldVisitPersistenceAssembler() {}

    public static FieldVisit toDomainFromPersistence(FieldVisitPersistenceEntity e) {
        if (e == null) return null;
        var d = new FieldVisit();
        d.setId(e.getId());
        d.setAgronomistId(e.getAgronomistId());
        d.setFarmId(e.getFarmId());
        d.setScheduledAt(e.getScheduledAt());
        d.setTag(e.getTag());
        d.setNoteTitle(e.getNoteTitle());
        d.setNoteBody(e.getNoteBody());
        d.setUrgent(e.getUrgent());
        d.setStatus(e.getStatus());
        return d;
    }

    public static FieldVisitPersistenceEntity toPersistenceFromDomain(FieldVisit d) {
        if (d == null) return null;
        var e = new FieldVisitPersistenceEntity();
        if (d.getId() != null) e.setId(d.getId());
        e.setAgronomistId(d.getAgronomistId());
        e.setFarmId(d.getFarmId());
        e.setScheduledAt(d.getScheduledAt());
        e.setTag(d.getTag());
        e.setNoteTitle(d.getNoteTitle());
        e.setNoteBody(d.getNoteBody());
        e.setUrgent(d.getUrgent());
        e.setStatus(d.getStatus());
        return e;
    }
}
