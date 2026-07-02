package com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.Farm;

import java.util.List;
import java.util.Optional;

public interface FarmQueryService {
    Optional<Farm> findById(Long farmId);
    List<Farm> findAllByUserId(Long userId);
    List<Farm> findAll();
}