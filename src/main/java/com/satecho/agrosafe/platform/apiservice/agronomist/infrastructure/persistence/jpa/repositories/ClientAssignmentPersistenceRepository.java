package com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.repositories;

import com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.entities.ClientAssignmentPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JPA Repository for {@link ClientAssignmentPersistenceEntity} database operations.
 * <p>
 * This interface handles direct database queries and CRUD operations for client assignments.
 * </p>
 */
@Repository
public interface ClientAssignmentPersistenceRepository extends JpaRepository<ClientAssignmentPersistenceEntity, Long> {

    /**
     * Retrieves all client assignments for a given agronomist user ID.
     *
     * @param agronomistUserId the ID of the agronomist user
     * @return a list of client assignment persistence entities
     */
    List<ClientAssignmentPersistenceEntity> findByAgronomistUserId(Long agronomistUserId);

    /**
     * Retrieves a specific client assignment by agronomist user ID and farmer user ID.
     *
     * @param agronomistUserId the ID of the agronomist user
     * @param farmerUserId the ID of the farmer user
     * @return an {@link Optional} containing the client assignment entity if found, or empty otherwise
     */
    Optional<ClientAssignmentPersistenceEntity> findByAgronomistUserIdAndFarmerUserId(Long agronomistUserId, Long farmerUserId);

    /**
     * Retrieves a client assignment for a given farmer user ID.
     *
     * @param farmerUserId the ID of the farmer user
     * @return an {@link Optional} containing the client assignment entity if found, or empty otherwise
     */
    Optional<ClientAssignmentPersistenceEntity> findByFarmerUserId(Long farmerUserId);

    /**
     * Checks if a client assignment exists for the given agronomist and farmer user IDs.
     *
     * @param agronomistUserId the ID of the agronomist user
     * @param farmerUserId the ID of the farmer user
     * @return {@code true} if the assignment exists, {@code false} otherwise
     */
    boolean existsByAgronomistUserIdAndFarmerUserId(Long agronomistUserId, Long farmerUserId);
}
