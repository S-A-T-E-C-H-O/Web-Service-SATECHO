package com.satecho.agrosafe.platform.apiservice.irrigation.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.irrigation.application.queryservices.ActuatorQueryService;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.aggregates.ActuatorLog;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.queries.GetActuatorLogsByDeviceQuery;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.repositories.ActuatorLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class ActuatorQueryServiceImpl implements ActuatorQueryService {

    private final ActuatorLogRepository actuatorLogRepository;

    public ActuatorQueryServiceImpl(ActuatorLogRepository actuatorLogRepository) {
        this.actuatorLogRepository = actuatorLogRepository;
    }

    @Override
    public List<ActuatorLog> handle(GetActuatorLogsByDeviceQuery query) {
        if (query.fromDate() != null && query.toDate() != null)
            return actuatorLogRepository.findByDeviceIdAndExecutedAtBetween(query.deviceId(), query.fromDate(), query.toDate(), query.limit());
        return actuatorLogRepository.findByDeviceIdOrderByExecutedAtDesc(query.deviceId(), query.limit());
    }
}
