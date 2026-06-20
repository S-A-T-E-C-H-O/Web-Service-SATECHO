package com.satecho.agrosafe.platform.apiservice.iot.application.internal.queryservices;

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

    public DeviceQueryServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Optional<Device> handle(GetDeviceByIdQuery query) {
        return deviceRepository.findById(query.deviceId());
    }

    @Override
    public List<Device> handle(GetAllDevicesByUserIdQuery query) {
        if (query.type() != null && query.status() != null)
            return deviceRepository.findByUserIdAndTypeAndStatus(query.userId(), query.type(), query.status());
        if (query.type() != null)
            return deviceRepository.findByUserIdAndType(query.userId(), query.type());
        if (query.status() != null)
            return deviceRepository.findByUserIdAndStatus(query.userId(), query.status());
        return deviceRepository.findByUserId(query.userId());
    }

    @Override
    public Optional<Device> handle(GetDeviceBySerialNumberQuery query) {
        return deviceRepository.findBySerialNumber(query.serialNumber());
    }
}
