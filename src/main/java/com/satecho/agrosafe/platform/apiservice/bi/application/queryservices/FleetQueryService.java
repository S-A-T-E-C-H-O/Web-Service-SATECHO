package com.satecho.agrosafe.platform.apiservice.bi.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.DiagnosticSession;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.FleetHealth;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.queries.GetDiagnosticQuery;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.aggregates.Device;

import java.util.List;
import java.util.Optional;

public interface FleetQueryService {
    FleetHealth getFleetHealth();
    List<Device> getFleetDevices(String status, Double batteryLevelBelow, Integer limit);
    Optional<DiagnosticSession> handle(GetDiagnosticQuery query);
}
