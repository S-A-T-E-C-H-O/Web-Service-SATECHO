package com.satecho.agrosafe.platform.apiservice.onboarding.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.FarmQueryService;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.Farm;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.repositories.FarmRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class FarmQueryServiceImpl implements FarmQueryService {

    private final FarmRepository farmRepository;

    public FarmQueryServiceImpl(FarmRepository farmRepository) {
        this.farmRepository = farmRepository;
    }

    @Override
    public Optional<Farm> findById(Long farmId) {
        return farmRepository.findById(farmId);
    }

    @Override
    public List<Farm> findAllByUserId(Long userId) {
        return farmRepository.findAllByUserId(userId);
    }

    @Override
    public List<Farm> findAll() {
        return farmRepository.findAll();
    }
}