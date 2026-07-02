package com.satecho.agrosafe.platform.apiservice.agronomist.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.agronomist.application.queryservices.ClientQueryService;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.queries.ClientDetail;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.repositories.ClientAssignmentRepository;
import com.satecho.agrosafe.platform.apiservice.iam.application.queryservices.UserQueryService;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.queries.GetUserByIdQuery;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.FarmQueryService;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.ZoneQueryService;
import com.satecho.agrosafe.platform.apiservice.soil.application.queryservices.TelemetryQueryService;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.queries.GetLatestReadingsByZoneQuery;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.valueobjects.MetricType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientQueryServiceImpl implements ClientQueryService {

    private final ClientAssignmentRepository clientAssignmentRepository;
    private final FarmQueryService farmQueryService;
    private final ZoneQueryService zoneQueryService;
    private final UserQueryService userQueryService;
    private final TelemetryQueryService telemetryQueryService;

    public ClientQueryServiceImpl(ClientAssignmentRepository clientAssignmentRepository, FarmQueryService farmQueryService,
                                   ZoneQueryService zoneQueryService, UserQueryService userQueryService,
                                   TelemetryQueryService telemetryQueryService) {
        this.clientAssignmentRepository = clientAssignmentRepository;
        this.farmQueryService = farmQueryService;
        this.zoneQueryService = zoneQueryService;
        this.userQueryService = userQueryService;
        this.telemetryQueryService = telemetryQueryService;
    }

    @Override
    public List<ClientDetail> findAssignedFarmersDetailed(Long agronomistUserId) {
        return clientAssignmentRepository.findByAgronomistUserId(agronomistUserId).stream()
                .filter(a -> Boolean.TRUE.equals(a.getActive()))
                .map(assignment -> toClientDetail(assignment.getId(), agronomistUserId, assignment.getFarmerUserId()))
                .filter(java.util.Objects::nonNull)
                .toList();
    }

    @Override
    public Long findAssignedAgronomistUserId(Long farmerUserId) {
        return clientAssignmentRepository.findByFarmerUserId(farmerUserId)
                .filter(a -> Boolean.TRUE.equals(a.getActive()))
                .map(a -> a.getAgronomistUserId())
                .orElse(null);
    }

    private ClientDetail toClientDetail(Long assignmentId, Long agronomistUserId, Long farmerUserId) {
        var farms = farmQueryService.findAllByUserId(farmerUserId);
        if (farms.isEmpty()) return null;
        var farm = farms.get(0);
        var zones = zoneQueryService.findAllByFarmId(farm.getId());
        var farmerName = userQueryService.handle(new GetUserByIdQuery(farmerUserId))
                .map(u -> u.getFullName()).orElse(null);

        Double soilHumidity = null, temperature = null, ec = null;
        if (!zones.isEmpty()) {
            var readings = telemetryQueryService.getLatestReadingsByZone(
                    new GetLatestReadingsByZoneQuery(zones.get(0).getId()));
            for (var r : readings) {
                if (r.getMetricType() == MetricType.SOIL_MOISTURE) soilHumidity = r.getValue();
                if (r.getMetricType() == MetricType.SOIL_TEMPERATURE) temperature = r.getValue();
                if (r.getMetricType() == MetricType.ELECTRICAL_CONDUCTIVITY) ec = r.getValue();
            }
        }

        return new ClientDetail(assignmentId, agronomistUserId, farmerUserId, farm.getId(), farmerName,
                farm.getName(), farm.getLocation(), farm.getCropType() != null ? farm.getCropType().name() : null,
                farm.getHectares(), zones.size(), soilHumidity, temperature, ec, true);
    }
}
