package com.satecho.agrosafe.platform.apiservice.agronomist.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.agronomist.application.queryservices.PriorityCasesQueryService;
import com.satecho.agrosafe.platform.apiservice.agronomist.interfaces.rest.resources.PriorityCaseResource;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.security.SecurityContextUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Agronomist Dashboard", description = "Priority cases panel (EP-009-US005)")
public class AgronomistDashboardController {

    private final PriorityCasesQueryService priorityCasesQueryService;

    public AgronomistDashboardController(PriorityCasesQueryService priorityCasesQueryService) {
        this.priorityCasesQueryService = priorityCasesQueryService;
    }

    @PreAuthorize("hasRole('AGRONOMIST')")
    @GetMapping("/priority-cases")
    public ResponseEntity<List<PriorityCaseResource>> getPriorityCases() {
        Long agronomistUserId = SecurityContextUtil.getCurrentUserId();
        var cases = priorityCasesQueryService.findPriorityCases(agronomistUserId).stream()
                .map(c -> new PriorityCaseResource(c.alertId(), c.farmerUserId(), c.farmerName(), c.farmId(),
                        c.farmName(), c.alertType(), c.severity(), c.createdAt()))
                .toList();
        return ResponseEntity.ok(cases);
    }
}
