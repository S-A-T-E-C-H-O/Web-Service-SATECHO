package com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.FieldVisit;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.repositories.FieldVisitRepository;
import com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.assemblers.FieldVisitPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.agronomist.infrastructure.persistence.jpa.repositories.FieldVisitPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the {@link FieldVisitRepository} interface.
 * <p>
 * This class provides the concrete implementation for managing field visits by adapting
 * domain operations to a JPA persistence repository.
 * </p>
 */
@Repository
public class FieldVisitRepositoryImpl implements FieldVisitRepository {

    /**
     * The JPA persistence repository used for data access operations on field visits.
     */
    private final FieldVisitPersistenceRepository persistenceRepository;

    /**
     * Constructs a new {@code FieldVisitRepositoryImpl} with the specified persistence repository.
     *
     * @param persistenceRepository the JPA persistence repository for field visits
     */
    public FieldVisitRepositoryImpl(FieldVisitPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    /**
     * Saves a field visit.
     *
     * @param visit the field visit domain entity to save
     * @return the saved field visit domain entity
     */
    @Override
    public FieldVisit save(FieldVisit visit) {
        var saved = persistenceRepository.save(FieldVisitPersistenceAssembler.toPersistenceFromDomain(visit));
        return FieldVisitPersistenceAssembler.toDomainFromPersistence(saved);
    }

    /**
     * Finds a field visit by its ID.
     *
     * @param id the ID of the field visit
     * @return an {@link Optional} containing the found field visit, or empty if not found
     */
    @Override
    public Optional<FieldVisit> findById(Long id) {
        return persistenceRepository.findById(id).map(FieldVisitPersistenceAssembler::toDomainFromPersistence);
    }

    /**
     * Finds field visits for a specific agronomist user, ordered by their scheduled date in ascending order.
     *
     * @param agronomistUserId the ID of the agronomist user
     * @return a list of field visits matching the agronomist user ID, ordered by scheduled date ascending
     */
    @Override
    public List<FieldVisit> findByAgronomistUserIdOrderByScheduledAtAsc(Long agronomistUserId) {
        return persistenceRepository.findByAgronomistUserIdOrderByScheduledAtAsc(agronomistUserId).stream()
                .map(FieldVisitPersistenceAssembler::toDomainFromPersistence).toList();
    }

    /**
     * Finds field visits for a specific farm, ordered by their scheduled date in descending order.
     *
     * @param farmId the ID of the farm
     * @return a list of field visits matching the farm ID, ordered by scheduled date descending
     */
    @Override
    public List<FieldVisit> findByFarmIdOrderByScheduledAtDesc(Long farmId) {
        return persistenceRepository.findByFarmIdOrderByScheduledAtDesc(farmId).stream()
                .map(FieldVisitPersistenceAssembler::toDomainFromPersistence).toList();
    }
}
