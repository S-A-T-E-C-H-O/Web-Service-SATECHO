package com.satecho.agrosafe.platform.apiservice.agronomist.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.agronomist.application.queryservices.FieldVisitQueryService;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.FieldVisit;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.repositories.FieldVisitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of the {@link FieldVisitQueryService} interface.
 * Handles query operations related to field visits.
 */
@Service
public class FieldVisitQueryServiceImpl implements FieldVisitQueryService {

    /**
     * Repository for accessing field visit data.
     */
    private final FieldVisitRepository fieldVisitRepository;

    /**
     * Constructs a {@code FieldVisitQueryServiceImpl} with the specified repository.
     *
     * @param fieldVisitRepository the repository for managing field visits
     */
    public FieldVisitQueryServiceImpl(FieldVisitRepository fieldVisitRepository) {
        this.fieldVisitRepository = fieldVisitRepository;
    }

    /**
     * Retrieves all field visits assigned to a specific agronomist, ordered by scheduling date ascending.
     *
     * @param agronomistUserId the identifier of the agronomist user
     * @return a List of FieldVisit aggregates
     */
    @Override
    public List<FieldVisit> findByAgronomistUserId(Long agronomistUserId) {
        return fieldVisitRepository.findByAgronomistUserIdOrderByScheduledAtAsc(agronomistUserId);
    }

    /**
     * Retrieves all field visits for a specific farm, ordered by scheduling date descending.
     *
     * @param farmId the identifier of the farm
     * @return a List of FieldVisit aggregates
     */
    @Override
    public List<FieldVisit> findByFarmId(Long farmId) {
        return fieldVisitRepository.findByFarmIdOrderByScheduledAtDesc(farmId);
    }
}
