package com.satecho.agrosafe.platform.apiservice.onboarding.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.Farm;

import java.util.List;
import java.util.Optional;

public interface FarmRepository {
    Optional<Farm> findById(Long id);
    List<Farm> findAllByUserId(Long userId);
    List<Farm> findAll();
    Farm save(Farm farm);
    void deleteById(Long id);
    boolean existsById(Long id);
}