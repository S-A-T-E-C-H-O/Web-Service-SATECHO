package com.satecho.agrosafe.platform.apiservice.bi.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.bi.application.commandservices.FleetCommandService;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.DiagnosticSession;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.FirmwareRelease;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.commands.BatchRegisterDevicesCommand;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.commands.CreateFirmwareCommand;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.commands.StartDiagnosticCommand;
import com.satecho.agrosafe.platform.apiservice.bi.domain.repositories.DiagnosticSessionRepository;
import com.satecho.agrosafe.platform.apiservice.bi.domain.repositories.FirmwareReleaseRepository;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.aggregates.Device;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.valueobjects.DeviceType;
import com.satecho.agrosafe.platform.apiservice.iot.infrastructure.persistence.jpa.assemblers.DevicePersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.iot.infrastructure.persistence.jpa.repositories.DevicePersistenceRepository;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class FleetCommandServiceImpl implements FleetCommandService {

    private final DevicePersistenceRepository deviceRepository;
    private final DiagnosticSessionRepository diagnosticSessionRepository;
    private final FirmwareReleaseRepository firmwareReleaseRepository;

    public FleetCommandServiceImpl(DevicePersistenceRepository deviceRepository,
                                   DiagnosticSessionRepository diagnosticSessionRepository,
                                   FirmwareReleaseRepository firmwareReleaseRepository) {
        this.deviceRepository = deviceRepository;
        this.diagnosticSessionRepository = diagnosticSessionRepository;
        this.firmwareReleaseRepository = firmwareReleaseRepository;
    }

    @Override
    public Result<List<String>, ApplicationError> batchRegisterDevices(BatchRegisterDevicesCommand command) {
        List<String> results = new ArrayList<>();
        for (var entry : command.devices()) {
            try {
                if (deviceRepository.existsBySerialNumber(entry.serialNumber())) {
                    results.add("FAILED: " + entry.serialNumber() + " - Serial number already exists");
                    continue;
                }
                DeviceType type = DeviceType.valueOf(entry.type().toUpperCase());
                Device device = new Device(command.userId(), entry.serialNumber(), type);
                deviceRepository.save(DevicePersistenceAssembler.toPersistenceFromDomain(device));
                results.add("OK: " + entry.serialNumber() + " - Registered as " + type.name());
            } catch (IllegalArgumentException e) {
                results.add("FAILED: " + entry.serialNumber() + " - " + e.getMessage());
            }
        }
        return Result.success(results);
    }

    @Override
    public Result<DiagnosticSession, ApplicationError> startDiagnostic(StartDiagnosticCommand command) {
        var deviceEntity = deviceRepository.findById(command.deviceId())
                .orElseThrow(() -> new IllegalArgumentException("Device not found: " + command.deviceId()));
        Device device = DevicePersistenceAssembler.toDomainFromPersistence(deviceEntity);

        DiagnosticSession session = new DiagnosticSession(device.getId());
        DiagnosticSession saved = diagnosticSessionRepository.save(session);

        var componentResults = "{\n" +
                "  \"connectivity\": \"" + (device.isWithinHeartbeatWindow() ? "PASS" : "WARN") + "\",\n" +
                "  \"battery\": \"" + (device.getBatteryLevel() != null && device.getBatteryLevel() > 20 ? "PASS" : "WARN") + "\",\n" +
                "  \"health\": \"" + ("HEALTHY".equals(device.getHealthStatus()) ? "PASS" : "FAIL") + "\",\n" +
                "  \"firmware\": \"" + (device.getFirmwareVersion() != null ? "OK" : "UNKNOWN") + "\"\n" +
                "}";

        String recommendation = generateRecommendation(device);

        saved.complete(componentResults, recommendation);
        return Result.success(diagnosticSessionRepository.save(saved));
    }

    @Override
    public Result<FirmwareRelease, ApplicationError> createFirmware(CreateFirmwareCommand command) {
        FirmwareRelease release = new FirmwareRelease(
                command.version(), command.deviceModel(),
                command.changelog(), command.binaryUrl());
        return Result.success(firmwareReleaseRepository.save(release));
    }

    private String generateRecommendation(Device device) {
        StringBuilder sb = new StringBuilder();
        if (!device.isWithinHeartbeatWindow()) {
            sb.append("Device is offline. Check network connectivity and power supply. ");
        }
        if (device.getBatteryLevel() != null && device.getBatteryLevel() <= 20) {
            sb.append("Battery level is low (").append(String.format("%.1f", device.getBatteryLevel()))
                    .append("%). Consider replacing or recharging batteries. ");
        }
        if (!"HEALTHY".equals(device.getHealthStatus())) {
            sb.append("Device health indicates issues: ").append(device.getHealthStatus()).append(". ");
        }
        if (sb.isEmpty()) {
            sb.append("All components operating within normal parameters.");
        }
        return sb.toString().trim();
    }
}
