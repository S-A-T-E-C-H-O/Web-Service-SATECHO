package com.satecho.agrosafe.platform.apiservice.onboarding.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.ZoneQueryService;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.IrrigationZone;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.repositories.ZoneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ZoneQueryServiceImpl implements ZoneQueryService {

    private final ZoneRepository zoneRepository;

    public ZoneQueryServiceImpl(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    @Override
    public Optional<IrrigationZone> findById(Long zoneId) {
        return zoneRepository.findById(zoneId);
    }

    @Override
    public Optional<IrrigationZone> findByDeviceId(Long deviceId) {
        return zoneRepository.findByDeviceId(deviceId);
    }

    @Override
    public List<IrrigationZone> findAllByFarmId(Long farmId) {
        return zoneRepository.findAllByFarmId(farmId);
    }
}