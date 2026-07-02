package com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.ClientAssignment;
import com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.entities.ClientAssignmentPersistenceEntity;

public final class ClientAssignmentPersistenceAssembler {
    private ClientAssignmentPersistenceAssembler() {}

    public static ClientAssignment toDomainFromPersistence(ClientAssignmentPersistenceEntity e) {
        if (e == null) return null;
        var d = new ClientAssignment();
        d.setId(e.getId());
        d.setAgronomistUserId(e.getAgronomistUserId());
        d.setFarmerUserId(e.getFarmerUserId());
        d.setAssignedAt(e.getAssignedAt());
        d.setActive(e.getActive());
        return d;
    }

    public static ClientAssignmentPersistenceEntity toPersistenceFromDomain(ClientAssignment d) {
        if (d == null) return null;
        var e = new ClientAssignmentPersistenceEntity();
        if (d.getId() != null) e.setId(d.getId());
        e.setAgronomistUserId(d.getAgronomistUserId());
        e.setFarmerUserId(d.getFarmerUserId());
        e.setAssignedAt(d.getAssignedAt());
        e.setActive(d.getActive());
        return e;
    }
}
