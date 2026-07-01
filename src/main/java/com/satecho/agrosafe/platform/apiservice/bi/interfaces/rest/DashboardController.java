package com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.bi.application.queryservices.DashboardQueryService;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.queries.GetAgronomistDashboardQuery;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.queries.GetFarmerDashboardQuery;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.queries.GetPriorityCasesQuery;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.security.SecurityContextUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/dashboard")
@PreAuthorize("isAuthenticated()")
public class DashboardController {

    private final DashboardQueryService dashboardQueryService;

    public DashboardController(DashboardQueryService dashboardQueryService) {
        this.dashboardQueryService = dashboardQueryService;
    }

    @GetMapping("/farmer")
    @PreAuthorize("hasRole('ROLE_FARMER')")
    public ResponseEntity<Map<String, Object>> getFarmerDashboard() {
        Long userId = SecurityContextUtil.getCurrentUserId();
        GetFarmerDashboardQuery query = new GetFarmerDashboardQuery(userId);
        Map<String, Object> dashboard = dashboardQueryService.getFarmerDashboard(query);
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/agronomist")
    @PreAuthorize("hasRole('ROLE_AGRONOMIST')")
    public ResponseEntity<Map<String, Object>> getAgronomistDashboard() {
        Long userId = SecurityContextUtil.getCurrentUserId();
        GetAgronomistDashboardQuery query = new GetAgronomistDashboardQuery(userId);
        Map<String, Object> dashboard = dashboardQueryService.getAgronomistDashboard(query);
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/priority-cases")
    public ResponseEntity<Map<String, Object>> getPriorityCases(
            @RequestParam(required = false, defaultValue = "10") Integer limit) {
        GetPriorityCasesQuery query = new GetPriorityCasesQuery(limit);
        Map<String, Object> cases = dashboardQueryService.getPriorityCases(query);
        return ResponseEntity.ok(cases);
    }
}
