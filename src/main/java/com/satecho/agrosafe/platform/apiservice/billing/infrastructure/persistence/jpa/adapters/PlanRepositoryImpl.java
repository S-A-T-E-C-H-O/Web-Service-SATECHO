package com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Plan;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects.PlanTier;
import com.satecho.agrosafe.platform.apiservice.billing.domain.repositories.PlanRepository;
import com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.assemblers.PlanPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.repositories.PlanPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JPA implementation adapter for the {@link PlanRepository} port.
 * Translates query operations between the domain and JPA persistence layer.
 */
@Repository
public class PlanRepositoryImpl implements PlanRepository {

    /**
     * Spring Data JPA database repository client.
     */
    private final PlanPersistenceRepository persistenceRepository;

    /**
     * Constructs a new PlanRepositoryImpl.
     *
     * @param persistenceRepository the underlying plan persistence repository
     */
    public PlanRepositoryImpl(PlanPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    /**
     * Saves a plan domain object as a persistence entity and converts the response back to a domain object.
     *
     * @param plan the plan domain model
     * @return the saved plan domain model
     */
    @Override
    public Plan save(Plan plan) {
        var saved = persistenceRepository.save(PlanPersistenceAssembler.toPersistenceFromDomain(plan));
        return PlanPersistenceAssembler.toDomainFromPersistence(saved);
    }

    /**
     * Retrieves all plans in the database, mapping them to the domain model representation.
     *
     * @return list of plan domain models
     */
    @Override
    public List<Plan> findAll() {
        return persistenceRepository.findAll().stream().map(PlanPersistenceAssembler::toDomainFromPersistence).toList();
    }

    /**
     * Finds a plan by its ID and maps it to domain space.
     *
     * @param id the plan ID
     * @return an {@link Optional} containing the plan domain model, or empty
     */
    @Override
    public Optional<Plan> findById(Long id) {
        return persistenceRepository.findById(id).map(PlanPersistenceAssembler::toDomainFromPersistence);
    }

    /**
     * Finds a plan by its tier name and maps it to domain space.
     *
     * @param tier the plan tier enum
     * @return an {@link Optional} containing the plan domain model, or empty
     */
    @Override
    public Optional<Plan> findByTier(PlanTier tier) {
        return persistenceRepository.findByTier(tier).map(PlanPersistenceAssembler::toDomainFromPersistence);
    }

    /**
     * Checks if a plan configuration exists for a given tier.
     *
     * @param tier the plan tier enum
     * @return true if it exists, false otherwise
     */
    @Override
    public boolean existsByTier(PlanTier tier) {
        return persistenceRepository.existsByTier(tier);
    }
}
