package com.satecho.agrosafe.platform.apiservice.bi.domain.repositories;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.FleetHealth;

import java.util.Optional;

public interface FleetHealthRepository {
    Optional<FleetHealth> findTopByOrderBySnapshotAtDesc();
    FleetHealth save(FleetHealth fleetHealth);
}
