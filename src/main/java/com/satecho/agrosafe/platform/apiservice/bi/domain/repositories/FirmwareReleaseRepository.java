package com.satecho.agrosafe.platform.apiservice.bi.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.FirmwareRelease;

import java.util.List;
import java.util.Optional;

public interface FirmwareReleaseRepository {
    Optional<FirmwareRelease> findById(Long id);
    List<FirmwareRelease> findByDeviceModelOrderByReleasedAtDesc(String deviceModel);
    Optional<FirmwareRelease> findByDeviceModelAndActiveTrue(String deviceModel);
    FirmwareRelease save(FirmwareRelease release);
}
