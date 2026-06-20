package com.satecho.agrosafe.platform.apiservice.soil.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.soil.application.queryservices.DiagnosisQueryService;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.aggregates.Diagnosis;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.queries.GetDiagnosisQuery;
import com.satecho.agrosafe.platform.apiservice.soil.domain.repositories.DiagnosisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class DiagnosisQueryServiceImpl implements DiagnosisQueryService {

    private final DiagnosisRepository diagnosisRepository;

    public DiagnosisQueryServiceImpl(DiagnosisRepository diagnosisRepository) {
        this.diagnosisRepository = diagnosisRepository;
    }

    @Override
    public Optional<Diagnosis> findById(GetDiagnosisQuery query) {
        return diagnosisRepository.findById(query.diagnosisId());
    }

    @Override
    public Optional<Diagnosis> findLatestByZoneId(Long zoneId) {
        return diagnosisRepository.findTopByZoneIdOrderByGeneratedAtDesc(zoneId);
    }
}
