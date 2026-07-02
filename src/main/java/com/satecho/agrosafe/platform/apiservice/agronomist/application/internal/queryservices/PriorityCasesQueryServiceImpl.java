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

@Service
public class PriorityCasesQueryServiceImpl implements PriorityCasesQueryService {

    private final ClientAssignmentRepository clientAssignmentRepository;
    private final FarmQueryService farmQueryService;
    private final AlertQueryService alertQueryService;
    private final UserQueryService userQueryService;

    public PriorityCasesQueryServiceImpl(ClientAssignmentRepository clientAssignmentRepository, FarmQueryService farmQueryService,
                                          AlertQueryService alertQueryService, UserQueryService userQueryService) {
        this.clientAssignmentRepository = clientAssignmentRepository;
        this.farmQueryService = farmQueryService;
        this.alertQueryService = alertQueryService;
        this.userQueryService = userQueryService;
    }

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
