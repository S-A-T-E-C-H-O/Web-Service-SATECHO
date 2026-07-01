package com.satecho.agrosafe.platform.apiservice.advisory.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.advisory.application.queryservices.FieldVisitQueryService;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.FieldVisit;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.queries.GetVisitsByAgronomistQuery;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.repositories.FieldVisitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class FieldVisitQueryServiceImpl implements FieldVisitQueryService {

    private final FieldVisitRepository repository;

    public FieldVisitQueryServiceImpl(FieldVisitRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<FieldVisit> handle(GetVisitsByAgronomistQuery query) {
        return repository.findByAgronomistId(query.agronomistId());
    }
}
