package com.satecho.agrosafe.platform.apiservice.agronomist.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.ClientAssignment;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link ClientAssignment} aggregates.
 *
 * @author Colegio
 * @version 1.0
 */
public interface ClientAssignmentRepository {
    /**
     * Saves a client assignment.
     *
     * @param assignment the client assignment to save
     * @return the saved client assignment
     */
    ClientAssignment save(ClientAssignment assignment);

    /**
     * Finds all client assignments for a given agronomist.
     *
     * @param agronomistUserId the unique identifier of the agronomist user
     * @return a list of client assignments associated with the agronomist
     */
    List<ClientAssignment> findByAgronomistUserId(Long agronomistUserId);

    /**
     * Finds a client assignment for a specific agronomist and farmer.
     *
     * @param agronomistUserId the unique identifier of the agronomist user
     * @param farmerUserId     the unique identifier of the farmer user
     * @return an {@link Optional} containing the client assignment if found, or empty otherwise
     */
    Optional<ClientAssignment> findByAgronomistUserIdAndFarmerUserId(Long agronomistUserId, Long farmerUserId);

    /**
     * Finds a client assignment for a given farmer.
     *
     * @param farmerUserId the unique identifier of the farmer user
     * @return an {@link Optional} containing the client assignment if found, or empty otherwise
     */
    Optional<ClientAssignment> findByFarmerUserId(Long farmerUserId);

    /**
     * Checks if a client assignment exists between a specific agronomist and farmer.
     *
     * @param agronomistUserId the unique identifier of the agronomist user
     * @param farmerUserId     the unique identifier of the farmer user
     * @return {@code true} if the assignment exists, {@code false} otherwise
     */
    boolean existsByAgronomistUserIdAndFarmerUserId(Long agronomistUserId, Long farmerUserId);
}
