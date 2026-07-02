package com.satecho.agrosafe.platform.apiservice.agronomist.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.agronomist.application.queryservices.FieldVisitQueryService;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.FieldVisit;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.repositories.FieldVisitRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FieldVisitQueryServiceImpl implements FieldVisitQueryService {

    private final FieldVisitRepository fieldVisitRepository;

    public FieldVisitQueryServiceImpl(FieldVisitRepository fieldVisitRepository) {
        this.fieldVisitRepository = fieldVisitRepository;
    }

    @Override
    public List<FieldVisit> findByAgronomistUserId(Long agronomistUserId) {
        return fieldVisitRepository.findByAgronomistUserIdOrderByScheduledAtAsc(agronomistUserId);
    }

    @Override
    public List<FieldVisit> findByFarmId(Long farmId) {
        return fieldVisitRepository.findByFarmIdOrderByScheduledAtDesc(farmId);
    }
}
