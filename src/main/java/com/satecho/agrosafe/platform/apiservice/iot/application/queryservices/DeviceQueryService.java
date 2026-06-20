package com.satecho.agrosafe.platform.apiservice.iot.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.iot.domain.model.aggregates.Device;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.queries.GetAllDevicesByUserIdQuery;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.queries.GetDeviceByIdQuery;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.queries.GetDeviceBySerialNumberQuery;

import java.util.List;
import java.util.Optional;

public interface DeviceQueryService {
    Optional<Device> handle(GetDeviceByIdQuery query);
    List<Device> handle(GetAllDevicesByUserIdQuery query);
    Optional<Device> handle(GetDeviceBySerialNumberQuery query);
}
