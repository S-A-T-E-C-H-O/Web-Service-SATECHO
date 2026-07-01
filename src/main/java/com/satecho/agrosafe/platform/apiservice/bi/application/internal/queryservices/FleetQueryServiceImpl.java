package com.satecho.agrosafe.platform.apiservice.bi.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.bi.application.queryservices.FleetQueryService;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.DiagnosticSession;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.FleetHealth;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.queries.GetDiagnosticQuery;
import com.satecho.agrosafe.platform.apiservice.bi.domain.repositories.DiagnosticSessionRepository;
import com.satecho.agrosafe.platform.apiservice.bi.domain.repositories.FleetHealthRepository;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.aggregates.Device;
import com.satecho.agrosafe.platform.apiservice.iot.infrastructure.persistence.jpa.assemblers.DevicePersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.iot.infrastructure.persistence.jpa.repositories.DevicePersistenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class FleetQueryServiceImpl implements FleetQueryService {

    private final FleetHealthRepository fleetHealthRepository;
    private final DiagnosticSessionRepository diagnosticSessionRepository;
    private final DevicePersistenceRepository deviceRepository;

    public FleetQueryServiceImpl(FleetHealthRepository fleetHealthRepository,
                                 DiagnosticSessionRepository diagnosticSessionRepository,
                                 DevicePersistenceRepository deviceRepository) {
        this.fleetHealthRepository = fleetHealthRepository;
        this.diagnosticSessionRepository = diagnosticSessionRepository;
        this.deviceRepository = deviceRepository;
    }

    @Override
    public FleetHealth getFleetHealth() {
        return fleetHealthRepository.findTopByOrderBySnapshotAtDesc()
                .orElseGet(this::computeCurrentFleetHealth);
    }

    @Override
    public List<Device> getFleetDevices(String status, Double batteryLevelBelow, Integer limit) {
        List<Device> allDevices = deviceRepository.findAll().stream()
                .map(DevicePersistenceAssembler::toDomainFromPersistence)
                .toList();

        if (status != null && !status.isBlank()) {
            allDevices = allDevices.stream()
                    .filter(d -> d.getStatus().name().equalsIgnoreCase(status))
                    .toList();
        }
        if (batteryLevelBelow != null) {
            allDevices = allDevices.stream()
                    .filter(d -> d.getBatteryLevel() != null && d.getBatteryLevel() < batteryLevelBelow)
                    .toList();
        }
        if (limit != null && limit > 0 && limit < allDevices.size()) {
            return allDevices.subList(0, limit);
        }
        return allDevices;
    }

    @Override
    public Optional<DiagnosticSession> handle(GetDiagnosticQuery query) {
        return diagnosticSessionRepository.findById(query.diagnosticId());
    }

    private FleetHealth computeCurrentFleetHealth() {
        List<Device> devices = deviceRepository.findAll().stream()
                .map(DevicePersistenceAssembler::toDomainFromPersistence)
                .toList();
        return buildFleetHealth(devices);
    }

    public FleetHealth buildFleetHealth(List<Device> devices) {
        int total = devices.size();
        int online = (int) devices.stream().filter(Device::isWithinHeartbeatWindow).count();
        int offline = total - online;
        int error = (int) devices.stream()
                .filter(d -> !"HEALTHY".equals(d.getHealthStatus())).count();
        int lowBattery = (int) devices.stream()
                .filter(d -> d.getBatteryLevel() != null && d.getBatteryLevel() < 20).count();

        var typeCounts = new java.util.LinkedHashMap<String, java.util.Map<String, Object>>();
        for (Device d : devices) {
            typeCounts.computeIfAbsent(d.getType().name(), k -> {
                var m = new java.util.LinkedHashMap<String, Object>();
                m.put("count", 0);
                m.put("online", 0);
                return m;
            });
        }
        for (Device d : devices) {
            @SuppressWarnings("unchecked")
            var m = (java.util.Map<String, Object>) typeCounts.get(d.getType().name());
            m.put("count", ((Number) m.get("count")).intValue() + 1);
            if (d.isWithinHeartbeatWindow()) {
                m.put("online", ((Number) m.get("online")).intValue() + 1);
            }
        }

        com.fasterxml.jackson.databind.node.ObjectNode typeJson = new com.fasterxml.jackson.databind.ObjectMapper()
                .createObjectNode();
        for (var entry : typeCounts.entrySet()) {
            var val = entry.getValue();
            com.fasterxml.jackson.databind.node.ObjectNode node = new com.fasterxml.jackson.databind.ObjectMapper()
                    .createObjectNode()
                    .put("count", ((Number) val.get("count")).intValue())
                    .put("online", ((Number) val.get("online")).intValue());
            typeJson.set(entry.getKey(), node);
        }
        String devicesByTypeJson = typeJson.toString();

        double avgSignal = devices.stream()
                .filter(d -> d.getBatteryLevel() != null)
                .mapToDouble(Device::getBatteryLevel)
                .average()
                .orElse(0);

        return new FleetHealth(total, online, offline, error, lowBattery,
                devicesByTypeJson, avgSignal);
    }
}
