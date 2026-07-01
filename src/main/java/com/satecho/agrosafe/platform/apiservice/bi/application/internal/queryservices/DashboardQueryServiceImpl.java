package com.satecho.agrosafe.platform.apiservice.bi.application.internal.queryservices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.satecho.agrosafe.platform.apiservice.bi.application.queryservices.DashboardQueryService;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.queries.GetAgronomistDashboardQuery;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.queries.GetFarmerDashboardQuery;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.queries.GetPriorityCasesQuery;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.aggregates.Device;
import com.satecho.agrosafe.platform.apiservice.iot.infrastructure.persistence.jpa.assemblers.DevicePersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.iot.infrastructure.persistence.jpa.repositories.DevicePersistenceRepository;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.Farm;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.IrrigationZone;
import com.satecho.agrosafe.platform.apiservice.onboarding.infrastructure.persistence.jpa.assemblers.FarmPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.onboarding.infrastructure.persistence.jpa.assemblers.ZonePersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.onboarding.infrastructure.persistence.jpa.repositories.FarmPersistenceRepository;
import com.satecho.agrosafe.platform.apiservice.onboarding.infrastructure.persistence.jpa.repositories.ZonePersistenceRepository;
import com.satecho.agrosafe.platform.apiservice.subscriptions.infrastructure.persistence.jpa.repositories.SubscriptionPersistenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class DashboardQueryServiceImpl implements DashboardQueryService {

    private final FarmPersistenceRepository farmRepository;
    private final ZonePersistenceRepository zoneRepository;
    private final DevicePersistenceRepository deviceRepository;
    private final SubscriptionPersistenceRepository subscriptionRepository;
    private final ObjectMapper objectMapper;

    public DashboardQueryServiceImpl(FarmPersistenceRepository farmRepository,
                                     ZonePersistenceRepository zoneRepository,
                                     DevicePersistenceRepository deviceRepository,
                                     SubscriptionPersistenceRepository subscriptionRepository,
                                     ObjectMapper objectMapper) {
        this.farmRepository = farmRepository;
        this.zoneRepository = zoneRepository;
        this.deviceRepository = deviceRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public Map<String, Object> getFarmerDashboard(GetFarmerDashboardQuery query) {
        Map<String, Object> dashboard = new LinkedHashMap<>();
        List<Farm> farms = farmRepository.findAllByUserId(query.userId()).stream()
                .map(FarmPersistenceAssembler::toDomainFromPersistence).toList();
        List<Device> devices = deviceRepository.findByUserId(query.userId()).stream()
                .map(DevicePersistenceAssembler::toDomainFromPersistence).toList();

        dashboard.put("totalFarms", farms.size());
        dashboard.put("totalDevices", devices.size());
        dashboard.put("onlineDevices", devices.stream().filter(Device::isWithinHeartbeatWindow).count());
        dashboard.put("offlineDevices", devices.stream().filter(d -> !d.isWithinHeartbeatWindow()).count());
        dashboard.put("errorDevices", devices.stream().filter(d -> !"HEALTHY".equals(d.getHealthStatus())).count());

        long totalZones = 0;
        for (Farm farm : farms) {
            totalZones += zoneRepository.findAllByFarmId(farm.getId()).size();
        }
        dashboard.put("totalZones", totalZones);

        ArrayNode farmsArray = objectMapper.createArrayNode();
        for (Farm farm : farms) {
            ObjectNode farmNode = objectMapper.createObjectNode();
            farmNode.put("id", farm.getId());
            farmNode.put("name", farm.getName());
            farmNode.put("cropType", farm.getCropType().name());
            farmNode.put("hectares", farm.getHectares());

            List<IrrigationZone> zones = zoneRepository.findAllByFarmId(farm.getId()).stream()
                    .map(ZonePersistenceAssembler::toDomainFromPersistence).toList();
            ArrayNode zonesArray = objectMapper.createArrayNode();
            for (IrrigationZone zone : zones) {
                ObjectNode zoneNode = objectMapper.createObjectNode();
                zoneNode.put("id", zone.getId());
                zoneNode.put("name", zone.getName());
                zonesArray.add(zoneNode);
            }
            farmNode.set("zones", zonesArray);
            farmsArray.add(farmNode);
        }
        dashboard.put("farms", farmsArray);

        ArrayNode devicesArray = objectMapper.createArrayNode();
        for (Device device : devices) {
            ObjectNode deviceNode = objectMapper.createObjectNode();
            deviceNode.put("id", device.getId());
            deviceNode.put("serialNumber", device.getSerialNumber());
            deviceNode.put("type", device.getType().name());
            deviceNode.put("status", device.getStatus().name());
            deviceNode.put("online", device.isWithinHeartbeatWindow());
            deviceNode.put("healthStatus", device.getHealthStatus());
            deviceNode.put("batteryLevel", device.getBatteryLevel());
            devicesArray.add(deviceNode);
        }
        dashboard.put("devices", devicesArray);

        return dashboard;
    }

    @Override
    public Map<String, Object> getAgronomistDashboard(GetAgronomistDashboardQuery query) {
        Map<String, Object> dashboard = new LinkedHashMap<>();
        List<Farm> allFarms = farmRepository.findAll().stream()
                .map(FarmPersistenceAssembler::toDomainFromPersistence).toList();

        dashboard.put("totalFarms", allFarms.size());
        dashboard.put("activeFarms", allFarms.size());

        List<Device> allDevices = deviceRepository.findAll().stream()
                .map(DevicePersistenceAssembler::toDomainFromPersistence).toList();
        dashboard.put("totalDevices", allDevices.size());
        dashboard.put("onlineDevices", allDevices.stream().filter(Device::isWithinHeartbeatWindow).count());
        dashboard.put("offlineDevices", allDevices.stream().filter(d -> !d.isWithinHeartbeatWindow()).count());
        dashboard.put("errorDevices", allDevices.stream().filter(d -> !"HEALTHY".equals(d.getHealthStatus())).count());
        dashboard.put("lowBatteryDevices", allDevices.stream()
                .filter(d -> d.getBatteryLevel() != null && d.getBatteryLevel() < 20).count());

        ArrayNode cropsArray = objectMapper.createArrayNode();
        Map<String, Long> cropCounts = new LinkedHashMap<>();
        for (Farm farm : allFarms) {
            cropCounts.merge(farm.getCropType().name(), 1L, Long::sum);
        }
        for (Map.Entry<String, Long> entry : cropCounts.entrySet()) {
            ObjectNode cropNode = objectMapper.createObjectNode();
            cropNode.put("crop", entry.getKey());
            cropNode.put("count", entry.getValue());
            cropsArray.add(cropNode);
        }
        dashboard.put("farmsByCrop", cropsArray);

        ArrayNode devicesArray = objectMapper.createArrayNode();
        for (Device device : allDevices) {
            ObjectNode deviceNode = objectMapper.createObjectNode();
            deviceNode.put("id", device.getId());
            deviceNode.put("serialNumber", device.getSerialNumber());
            deviceNode.put("type", device.getType().name());
            deviceNode.put("status", device.getStatus().name());
            deviceNode.put("online", device.isWithinHeartbeatWindow());
            deviceNode.put("healthStatus", device.getHealthStatus());
            deviceNode.put("batteryLevel", device.getBatteryLevel());
            devicesArray.add(deviceNode);
        }
        dashboard.put("devices", devicesArray);

        return dashboard;
    }

    @Override
    public Map<String, Object> getPriorityCases(GetPriorityCasesQuery query) {
        Map<String, Object> result = new LinkedHashMap<>();
        List<Device> allDevices = deviceRepository.findAll().stream()
                .map(DevicePersistenceAssembler::toDomainFromPersistence).toList();

        ArrayNode cases = objectMapper.createArrayNode();

        allDevices.stream()
                .filter(d -> !d.isWithinHeartbeatWindow())
                .limit(query.limit())
                .forEach(device -> {
                    ObjectNode caseNode = objectMapper.createObjectNode();
                    caseNode.put("id", device.getId());
                    caseNode.put("deviceId", device.getId());
                    caseNode.put("serialNumber", device.getSerialNumber());
                    caseNode.put("type", device.getType().name());
                    caseNode.put("issue", "Device offline - last heartbeat: " +
                            (device.getLastHeartbeatAt() != null
                                    ? device.getLastHeartbeatAt().toString() : "never"));
                    caseNode.put("severity", "HIGH");
                    caseNode.put("healthStatus", device.getHealthStatus());
                    cases.add(caseNode);
                });

        allDevices.stream()
                .filter(d -> !"HEALTHY".equals(d.getHealthStatus()) && d.isWithinHeartbeatWindow())
                .limit(query.limit())
                .forEach(device -> {
                    ObjectNode caseNode = objectMapper.createObjectNode();
                    caseNode.put("id", device.getId());
                    caseNode.put("deviceId", device.getId());
                    caseNode.put("serialNumber", device.getSerialNumber());
                    caseNode.put("type", device.getType().name());
                    caseNode.put("issue", "Device health degraded: " + device.getHealthStatus());
                    caseNode.put("severity", "MEDIUM");
                    caseNode.put("healthStatus", device.getHealthStatus());
                    cases.add(caseNode);
                });

        allDevices.stream()
                .filter(d -> d.getBatteryLevel() != null && d.getBatteryLevel() < 20
                        && d.isWithinHeartbeatWindow() && "HEALTHY".equals(d.getHealthStatus()))
                .limit(query.limit())
                .forEach(device -> {
                    ObjectNode caseNode = objectMapper.createObjectNode();
                    caseNode.put("id", device.getId());
                    caseNode.put("deviceId", device.getId());
                    caseNode.put("serialNumber", device.getSerialNumber());
                    caseNode.put("type", device.getType().name());
                    caseNode.put("issue", "Low battery: " +
                            String.format("%.1f%%", device.getBatteryLevel()));
                    caseNode.put("severity", "LOW");
                    caseNode.put("healthStatus", device.getHealthStatus());
                    cases.add(caseNode);
                });

        result.put("totalPriorityCases", cases.size());
        result.put("cases", cases);

        return result;
    }
}
