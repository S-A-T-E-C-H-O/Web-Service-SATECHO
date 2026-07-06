package com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.ClientAssignment;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.repositories.ClientAssignmentRepository;
import com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.assemblers.ClientAssignmentPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.repositories.ClientAssignmentPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link ClientAssignmentRepository} interface.
 * <p>
 * This class provides the concrete implementation for managing client assignments by adapting
 * domain operations to a JPA persistence repository.
 * </p>
 */
@Repository
public class ClientAssignmentRepositoryImpl implements ClientAssignmentRepository {

    /**
     * The JPA persistence repository used for data access operations.
     */
    private final ClientAssignmentPersistenceRepository persistenceRepository;

    /**
     * Constructs a new {@code ClientAssignmentRepositoryImpl} with the specified persistence repository.
     *
     * @param persistenceRepository the JPA persistence repository for client assignments
     */
    public ClientAssignmentRepositoryImpl(ClientAssignmentPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    /**
     * Saves a client assignment.
     *
     * @param assignment the client assignment domain entity to save
     * @return the saved client assignment domain entity
     */
    @Override
    public ClientAssignment save(ClientAssignment assignment) {
        var saved = persistenceRepository.save(ClientAssignmentPersistenceAssembler.toPersistenceFromDomain(assignment));
        return ClientAssignmentPersistenceAssembler.toDomainFromPersistence(saved);
    }

    /**
     * Finds client assignments associated with a specific agronomist user ID.
     *
     * @param agronomistUserId the ID of the agronomist user
     * @return a list of client assignments matching the agronomist user ID
     */
    @Override
    public List<ClientAssignment> findByAgronomistUserId(Long agronomistUserId) {
        return persistenceRepository.findByAgronomistUserId(agronomistUserId).stream()
                .map(ClientAssignmentPersistenceAssembler::toDomainFromPersistence).toList();
    }

    /**
     * Finds a client assignment by the agronomist user ID and farmer user ID.
     *
     * @param agronomistUserId the ID of the agronomist user
     * @param farmerUserId the ID of the farmer user
     * @return an {@link Optional} containing the found client assignment, or empty if not found
     */
    @Override
    public Optional<ClientAssignment> findByAgronomistUserIdAndFarmerUserId(Long agronomistUserId, Long farmerUserId) {
        return persistenceRepository.findByAgronomistUserIdAndFarmerUserId(agronomistUserId, farmerUserId)
                .map(ClientAssignmentPersistenceAssembler::toDomainFromPersistence);
    }

    /**
     * Finds a client assignment by the farmer user ID.
     *
     * @param farmerUserId the ID of the farmer user
     * @return an {@link Optional} containing the found client assignment, or empty if not found
     */
    @Override
    public Optional<ClientAssignment> findByFarmerUserId(Long farmerUserId) {
        return persistenceRepository.findByFarmerUserId(farmerUserId)
                .map(ClientAssignmentPersistenceAssembler::toDomainFromPersistence);
    }

    /**
     * Checks if a client assignment exists for the given agronomist and farmer user IDs.
     *
     * @param agronomistUserId the ID of the agronomist user
     * @param farmerUserId the ID of the farmer user
     * @return {@code true} if an assignment exists, {@code false} otherwise
     */
    @Override
    public boolean existsByAgronomistUserIdAndFarmerUserId(Long agronomistUserId, Long farmerUserId) {
        return persistenceRepository.existsByAgronomistUserIdAndFarmerUserId(agronomistUserId, farmerUserId);
    }
}
