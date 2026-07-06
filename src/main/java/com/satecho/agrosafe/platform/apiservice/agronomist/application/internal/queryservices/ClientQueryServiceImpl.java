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

/**
 * Implementation of the {@link ClientQueryService} interface.
 * Handles query operations related to clients.
 */
@Service
public class ClientQueryServiceImpl implements ClientQueryService {

    /**
     * Repository for accessing client assignment data.
     */
    private final ClientAssignmentRepository clientAssignmentRepository;

    /**
     * Service for querying farm information.
     */
    private final FarmQueryService farmQueryService;

    /**
     * Service for querying zone information.
     */
    private final ZoneQueryService zoneQueryService;

    /**
     * Service for querying user information.
     */
    private final UserQueryService userQueryService;

    /**
     * Service for querying telemetry information.
     */
    private final TelemetryQueryService telemetryQueryService;

    /**
     * Constructs a {@code ClientQueryServiceImpl} with the specified repositories and services.
     *
     * @param clientAssignmentRepository the repository for managing client assignments
     * @param farmQueryService the service for querying farms
     * @param zoneQueryService the service for querying zones
     * @param userQueryService the service for querying users
     * @param telemetryQueryService the service for querying telemetry data
     */
    public ClientQueryServiceImpl(ClientAssignmentRepository clientAssignmentRepository, FarmQueryService farmQueryService,
                                   ZoneQueryService zoneQueryService, UserQueryService userQueryService,
                                   TelemetryQueryService telemetryQueryService) {
        this.clientAssignmentRepository = clientAssignmentRepository;
        this.farmQueryService = farmQueryService;
        this.zoneQueryService = zoneQueryService;
        this.userQueryService = userQueryService;
        this.telemetryQueryService = telemetryQueryService;
    }

    /**
     * Retrieves detailed information of all farmers assigned to a specific agronomist.
     *
     * @param agronomistUserId the identifier of the agronomist user
     * @return a List of ClientDetail containing detailed client information
     */
    @Override
    public List<ClientDetail> findAssignedFarmersDetailed(Long agronomistUserId) {
        return clientAssignmentRepository.findByAgronomistUserId(agronomistUserId).stream()
                .filter(a -> Boolean.TRUE.equals(a.getActive()))
                .map(assignment -> toClientDetail(assignment.getId(), agronomistUserId, assignment.getFarmerUserId()))
                .filter(java.util.Objects::nonNull)
                .toList();
    }

    /**
     * Retrieves the agronomist user identifier assigned to a specific farmer.
     *
     * @param farmerUserId the identifier of the farmer user
     * @return the agronomist user identifier, or null if not found or inactive
     */
    @Override
    public Long findAssignedAgronomistUserId(Long farmerUserId) {
        return clientAssignmentRepository.findByFarmerUserId(farmerUserId)
                .filter(a -> Boolean.TRUE.equals(a.getActive()))
                .map(a -> a.getAgronomistUserId())
                .orElse(null);
    }

    /**
     * Maps client assignment details to a ClientDetail DTO.
     *
     * @param assignmentId the assignment identifier
     * @param agronomistUserId the agronomist user identifier
     * @param farmerUserId the farmer user identifier
     * @return the mapped ClientDetail, or null if no farms are found for the farmer
     */
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
