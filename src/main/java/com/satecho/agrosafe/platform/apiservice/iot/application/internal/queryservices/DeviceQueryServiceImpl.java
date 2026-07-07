package com.satecho.agrosafe.platform.apiservice.iot.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.edge.application.services.DemoSharedDeviceLinkService;
import com.satecho.agrosafe.platform.apiservice.iot.application.queryservices.DeviceQueryService;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.aggregates.Device;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.queries.GetAllDevicesByUserIdQuery;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.queries.GetDeviceByIdQuery;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.queries.GetDeviceBySerialNumberQuery;
import com.satecho.agrosafe.platform.apiservice.iot.domain.repositories.DeviceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class DeviceQueryServiceImpl implements DeviceQueryService {

    private final DeviceRepository deviceRepository;
    private final DemoSharedDeviceLinkService demoSharedDeviceLinkService;

    public DeviceQueryServiceImpl(DeviceRepository deviceRepository,
                                  DemoSharedDeviceLinkService demoSharedDeviceLinkService) {
        this.deviceRepository = deviceRepository;
        this.demoSharedDeviceLinkService = demoSharedDeviceLinkService;
    }

    @Override
    public Optional<Device> handle(GetDeviceByIdQuery query) {
        return deviceRepository.findById(query.deviceId());
    }

    @Override
    public List<Device> handle(GetAllDevicesByUserIdQuery query) {
        List<Device> devices;
        if (query.type() != null && query.status() != null)
            devices = deviceRepository.findByUserIdAndTypeAndStatus(query.userId(), query.type(), query.status());
        else if (query.type() != null)
            devices = deviceRepository.findByUserIdAndType(query.userId(), query.type());
        else if (query.status() != null)
            devices = deviceRepository.findByUserIdAndStatus(query.userId(), query.status());
        else
            devices = deviceRepository.findByUserId(query.userId());

        var visibleDeviceIds = new java.util.HashSet<Long>();
        devices.forEach(device -> visibleDeviceIds.add(device.getId()));
        var demoDevices = demoSharedDeviceLinkService.findActiveByUserId(query.userId()).stream()
                .flatMap(link -> deviceRepository.findById(link.getPhysicalDeviceId()).stream())
                .filter(device -> query.type() == null || device.getType() == query.type())
                .filter(device -> query.status() == null || device.getStatus() == query.status())
                .filter(device -> visibleDeviceIds.add(device.getId()))
                .map(device -> asVisibleDemoDevice(device, query.userId()))
                .toList();

        if (demoDevices.isEmpty()) return devices;
        var merged = new java.util.ArrayList<Device>(devices);
        merged.addAll(demoDevices);
        return merged;
    }

    @Override
    public Optional<Device> handle(GetDeviceBySerialNumberQuery query) {
        return deviceRepository.findBySerialNumber(query.serialNumber());
    }

    @Override
    public List<Device> findAll() {
        return deviceRepository.findAll();
    }

    private Device asVisibleDemoDevice(Device source, Long visibleUserId) {
        var copy = new Device();
        copy.setId(source.getId());
        copy.setUserId(visibleUserId);
        copy.setSerialNumber(source.getSerialNumber());
        copy.setType(source.getType());
        copy.setStatus(source.getStatus());
        copy.setCredentials(source.getCredentials());
        copy.setFirmwareVersion(source.getFirmwareVersion());
        copy.setBatteryLevel(source.getBatteryLevel());
        copy.setLastHeartbeatAt(source.getLastHeartbeatAt());
        copy.setLastTelemetryAt(source.getLastTelemetryAt());
        copy.setHealthStatus(source.getHealthStatus());
        return copy;
    }
}
