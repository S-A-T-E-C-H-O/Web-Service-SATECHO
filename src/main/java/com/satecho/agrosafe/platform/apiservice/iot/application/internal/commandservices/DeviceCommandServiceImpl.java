package com.satecho.agrosafe.platform.apiservice.iot.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.iot.application.commandservices.DeviceCommandService;
import com.satecho.agrosafe.platform.apiservice.iot.domain.exceptions.DeviceNotFoundException;
import com.satecho.agrosafe.platform.apiservice.iot.domain.exceptions.DuplicateSerialNumberException;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.aggregates.Device;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.commands.*;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.events.HeartbeatReceivedEvent;
import com.satecho.agrosafe.platform.apiservice.iot.domain.repositories.DeviceRepository;
import com.satecho.agrosafe.platform.apiservice.iot.interfaces.rest.resources.RegisterDeviceResource;
import com.satecho.agrosafe.platform.apiservice.billing.application.queryservices.PlanLimitsService;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DeviceCommandServiceImpl implements DeviceCommandService {

    private final DeviceRepository deviceRepository;
    private final PlanLimitsService planLimitsService;

    public DeviceCommandServiceImpl(DeviceRepository deviceRepository, PlanLimitsService planLimitsService) {
        this.deviceRepository = deviceRepository;
        this.planLimitsService = planLimitsService;
    }

    @Override
    public Result<Device, ApplicationError> handle(RegisterDeviceCommand command) {
        if (deviceRepository.existsBySerialNumber(command.serialNumber())) {
            return Result.failure(ApplicationError.conflict("Device", "Duplicate serial number: " + command.serialNumber()));
        }
        if (!planLimitsService.canRegisterDevice(command.userId())) {
            return Result.failure(ApplicationError.forbidden("Device", "Farmer's plan device limit has been reached"));
        }
        var device = new Device(command.userId(), command.serialNumber(), command.type());
        return Result.success(deviceRepository.save(device));
    }

    @Override
    public Result<Device, ApplicationError> handle(ActivateDeviceCommand command) {
        var device = deviceRepository.findById(command.deviceId())
                .orElseThrow(() -> new DeviceNotFoundException(command.deviceId()));
        device.activate(command.certificateThumbprint());
        return Result.success(deviceRepository.save(device));
    }

    @Override
    public Result<Device, ApplicationError> handle(UpdateFirmwareCommand command) {
        var device = deviceRepository.findById(command.deviceId())
                .orElseThrow(() -> new DeviceNotFoundException(command.deviceId()));
        device.updateFirmware(command.firmwareVersion());
        return Result.success(deviceRepository.save(device));
    }

    @Override
    public Result<Void, ApplicationError> handleDeactivate(Long deviceId) {
        var device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new DeviceNotFoundException(deviceId));
        device.deactivate();
        deviceRepository.save(device);
        return Result.success(null);
    }

    @Override
    public Result<Void, ApplicationError> handleDecommission(Long deviceId) {
        var device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new DeviceNotFoundException(deviceId));
        device.decommission();
        deviceRepository.save(device);
        return Result.success(null);
    }

    @Override
    public Result<Device, ApplicationError> recordHeartbeat(Long deviceId, Double batteryLevel) {
        var device = deviceRepository.findById(deviceId)
                .orElseThrow(() -> new DeviceNotFoundException(deviceId));
        device.recordHeartbeat();
        device.recordTelemetry();
        if (batteryLevel != null) device.updateBatteryLevel(batteryLevel);
        device.addDomainEvent(new HeartbeatReceivedEvent(deviceId, batteryLevel));
        return Result.success(deviceRepository.save(device));
    }

    @Override
    public Result<BatchRegisterResult, ApplicationError> handleBatchRegister(List<RegisterDeviceResource> resources) {
        List<BatchRegisterResult.Entry> entries = new ArrayList<>();
        int succeeded = 0, failed = 0;
        for (var resource : resources) {
            try {
                if (deviceRepository.existsBySerialNumber(resource.serialNumber())) {
                    entries.add(new BatchRegisterResult.Entry(resource.serialNumber(), "FAILED", null, "Duplicate serial number"));
                    failed++;
                } else if (!planLimitsService.canRegisterDevice(resource.farmerId())) {
                    entries.add(new BatchRegisterResult.Entry(resource.serialNumber(), "FAILED", null, "Farmer's plan device limit has been reached"));
                    failed++;
                } else {
                    var device = new Device(resource.farmerId(), resource.serialNumber(), resource.type());
                    var saved = deviceRepository.save(device);
                    entries.add(new BatchRegisterResult.Entry(resource.serialNumber(), "SUCCESS", saved.getId(), null));
                    succeeded++;
                }
            } catch (Exception e) {
                entries.add(new BatchRegisterResult.Entry(resource.serialNumber(), "FAILED", null, e.getMessage()));
                failed++;
            }
        }
        return Result.success(new BatchRegisterResult(resources.size(), succeeded, failed, entries));
    }
}
