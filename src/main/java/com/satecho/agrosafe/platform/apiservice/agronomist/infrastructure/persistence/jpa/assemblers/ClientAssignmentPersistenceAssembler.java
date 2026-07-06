package com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.assemblers;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.ClientAssignment;
import com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.entities.ClientAssignmentPersistenceEntity;

/**
 * Assembler class that converts between {@link ClientAssignment} domain aggregates
 * and {@link ClientAssignmentPersistenceEntity} database entities.
 * <p>
 * This class consists exclusively of static methods to perform the mapping.
 * </p>
 */
public final class ClientAssignmentPersistenceAssembler {
    
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ClientAssignmentPersistenceAssembler() {}

    /**
     * Converts a {@link ClientAssignmentPersistenceEntity} to a {@link ClientAssignment} domain aggregate.
     *
     * @param e the persistence entity to convert
     * @return the domain aggregate, or {@code null} if the input is null
     */
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

    /**
     * Converts a {@link ClientAssignment} domain aggregate to a {@link ClientAssignmentPersistenceEntity}.
     *
     * @param d the domain aggregate to convert
     * @return the persistence entity, or {@code null} if the input is null
     */
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
