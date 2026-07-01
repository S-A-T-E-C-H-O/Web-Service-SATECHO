package com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.AgronomistClient;
import com.satecho.agrosafe.platform.apiservice.advisory.infrastructure.persistence.jpa.entities.AgronomistClientPersistenceEntity;

public final class AgronomistClientPersistenceAssembler {
    private AgronomistClientPersistenceAssembler() {}

    public static AgronomistClient toDomainFromPersistence(AgronomistClientPersistenceEntity e) {
        if (e == null) return null;
        var d = new AgronomistClient();
        d.setId(e.getId());
        d.setAgronomistId(e.getAgronomistId());
        d.setFarmerId(e.getFarmerId());
        d.setLinkedAt(e.getLinkedAt());
        d.setActive(e.getActive());
        d.setNotes(e.getNotes());
        return d;
    }

    public static AgronomistClientPersistenceEntity toPersistenceFromDomain(AgronomistClient d) {
        if (d == null) return null;
        var e = new AgronomistClientPersistenceEntity();
        if (d.getId() != null) e.setId(d.getId());
        e.setAgronomistId(d.getAgronomistId());
        e.setFarmerId(d.getFarmerId());
        e.setLinkedAt(d.getLinkedAt());
        e.setActive(d.getActive());
        e.setNotes(d.getNotes());
        return e;
    }
}
