package com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.bi.application.queryservices.AnalyticsQueryService;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.AnalyticsSnapshot;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.queries.GetChurnAnalysisQuery;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.queries.GetExecutiveAnalyticsQuery;
import com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.resources.ChurnAnalyticsResource;
import com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.resources.ExecutiveAnalyticsResource;
import com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.transform.ChurnAnalyticsResourceFromEntityAssembler;
import com.satecho.agrosafe.platform.apiservice.bi.interfaces.rest.transform.ExecutiveAnalyticsResourceFromEntityAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/analytics")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AnalyticsController {

    private final AnalyticsQueryService analyticsQueryService;

    public AnalyticsController(AnalyticsQueryService analyticsQueryService) {
        this.analyticsQueryService = analyticsQueryService;
    }

    @GetMapping("/executive")
    public ResponseEntity<ExecutiveAnalyticsResource> getExecutiveAnalytics(
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to) {
        GetExecutiveAnalyticsQuery query = new GetExecutiveAnalyticsQuery(period, from, to);
        AnalyticsSnapshot snapshot = analyticsQueryService.getExecutiveAnalytics(query);
        ExecutiveAnalyticsResource resource = ExecutiveAnalyticsResourceFromEntityAssembler
                .toResourceFromEntity(snapshot);
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/churn")
    public ResponseEntity<ChurnAnalyticsResource> getChurnAnalysis(
            @RequestParam(required = false) String segment) {
        GetChurnAnalysisQuery query = new GetChurnAnalysisQuery(segment);
        AnalyticsSnapshot snapshot = analyticsQueryService.getChurnAnalysis(query);
        ChurnAnalyticsResource resource = ChurnAnalyticsResourceFromEntityAssembler
                .toResourceFromEntity(snapshot, segment);
        return ResponseEntity.ok(resource);
    }
}
