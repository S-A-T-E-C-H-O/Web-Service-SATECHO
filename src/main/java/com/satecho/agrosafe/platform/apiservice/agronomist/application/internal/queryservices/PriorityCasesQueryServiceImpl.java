package com.satecho.agrosafe.platform.apiservice.agronomist.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.advisory.application.queryservices.AlertQueryService;
import com.satecho.agrosafe.platform.apiservice.agronomist.application.queryservices.PriorityCasesQueryService;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.queries.PriorityCase;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.repositories.ClientAssignmentRepository;
import com.satecho.agrosafe.platform.apiservice.iam.application.queryservices.UserQueryService;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.queries.GetUserByIdQuery;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.FarmQueryService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link PriorityCasesQueryService} interface.
 * Handles querying priority cases (farms with active alerts) assigned to agronomists.
 */
@Service
public class PriorityCasesQueryServiceImpl implements PriorityCasesQueryService {

    /**
     * Repository for accessing client assignment data.
     */
    private final ClientAssignmentRepository clientAssignmentRepository;

    /**
     * Service for querying farm information.
     */
    private final FarmQueryService farmQueryService;

    /**
     * Service for querying alert information.
     */
    private final AlertQueryService alertQueryService;

    /**
     * Service for querying user information.
     */
    private final UserQueryService userQueryService;

    /**
     * Constructs a {@code PriorityCasesQueryServiceImpl} with the specified repositories and services.
     *
     * @param clientAssignmentRepository the repository for managing client assignments
     * @param farmQueryService the service for querying farms
     * @param alertQueryService the service for querying alerts
     * @param userQueryService the service for querying users
     */
    public PriorityCasesQueryServiceImpl(ClientAssignmentRepository clientAssignmentRepository, FarmQueryService farmQueryService,
                                           AlertQueryService alertQueryService, UserQueryService userQueryService) {
        this.clientAssignmentRepository = clientAssignmentRepository;
        this.farmQueryService = farmQueryService;
        this.alertQueryService = alertQueryService;
        this.userQueryService = userQueryService;
    }

    /**
     * Retrieves all priority cases for a specific agronomist user, representing assigned farmers' farms with active alerts.
     *
     * @param agronomistUserId the identifier of the agronomist user
     * @return a List of PriorityCase objects containing active alert and client details
     */
    @Override
    public List<PriorityCase> findPriorityCases(Long agronomistUserId) {
        List<PriorityCase> cases = new ArrayList<>();
        var assignments = clientAssignmentRepository.findByAgronomistUserId(agronomistUserId);
        for (var assignment : assignments) {
            if (!Boolean.TRUE.equals(assignment.getActive())) continue;
            var farmerUserId = assignment.getFarmerUserId();
            var farmerName = userQueryService.handle(new GetUserByIdQuery(farmerUserId))
                    .map(u -> u.getFullName()).orElse(null);
            for (var farm : farmQueryService.findAllByUserId(farmerUserId)) {
                var activeAlerts = alertQueryService.findActiveByFarmId(farm.getId());
                for (var alert : activeAlerts) {
                    cases.add(new PriorityCase(alert.getId(), farmerUserId, farmerName, farm.getId(), farm.getName(),
                            alert.getAlertType().name(), alert.getSeverity().name(), alert.getCreatedAt()));
                }
            }
        }
        return cases;
    }
}
