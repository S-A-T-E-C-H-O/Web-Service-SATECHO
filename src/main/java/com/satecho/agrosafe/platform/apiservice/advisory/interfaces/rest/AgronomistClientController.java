package com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.advisory.application.commandservices.AgronomistClientCommandService;
import com.satecho.agrosafe.platform.apiservice.advisory.application.queryservices.AgronomistClientQueryService;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.AgronomistClient;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.commands.LinkClientCommand;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.commands.UnlinkClientCommand;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.exceptions.ClientLinkNotFoundException;
import com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.resources.AgronomistSummaryResource;
import com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.resources.ClientDetailResource;
import com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.resources.ClientResource;
import com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.resources.LinkFarmerResource;
import com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.transform.AgronomistSummaryResourceAssembler;
import com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.transform.ClientResourceFromEntityAssembler;
import com.satecho.agrosafe.platform.apiservice.iam.application.queryservices.UserQueryService;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.queries.GetUserByIdQuery;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.FarmQueryService;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.ZoneQueryService;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.Farm;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.security.SecurityContextUtil;
import com.satecho.agrosafe.platform.apiservice.shared.interfaces.rest.transform.ResponseEntityAssembler;
import com.satecho.agrosafe.platform.apiservice.soil.application.queryservices.TelemetryQueryService;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.queries.GetLatestReadingsByZoneQuery;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.valueobjects.MetricType;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Agronomist Clients", description = "Agronomist client management endpoints")
@PreAuthorize("isAuthenticated()")
public class AgronomistClientController {

    private final AgronomistClientCommandService agronomistClientCommandService;
    private final AgronomistClientQueryService agronomistClientQueryService;
    private final FarmQueryService farmQueryService;
    private final ZoneQueryService zoneQueryService;
    private final TelemetryQueryService telemetryQueryService;
    private final UserQueryService userQueryService;

    public AgronomistClientController(AgronomistClientCommandService agronomistClientCommandService,
                                      AgronomistClientQueryService agronomistClientQueryService,
                                      FarmQueryService farmQueryService,
                                      ZoneQueryService zoneQueryService,
                                      TelemetryQueryService telemetryQueryService,
                                      UserQueryService userQueryService) {
        this.agronomistClientCommandService = agronomistClientCommandService;
        this.agronomistClientQueryService = agronomistClientQueryService;
        this.farmQueryService = farmQueryService;
        this.zoneQueryService = zoneQueryService;
        this.telemetryQueryService = telemetryQueryService;
        this.userQueryService = userQueryService;
    }

    @GetMapping("/agronomist/clients/detailed")
    @PreAuthorize("hasRole('AGRONOMIST')")
    public ResponseEntity<List<ClientDetailResource>> getAgronomistClientsDetailed() {
        Long agronomistId = SecurityContextUtil.getCurrentUserId();
        List<AgronomistClient> clients = agronomistClientQueryService.findByAgronomistId(agronomistId);
        var resources = clients.stream().map(client -> {
            var farms = farmQueryService.findAllByUserId(client.getFarmerId());
            Farm farm = farms.isEmpty() ? null : farms.get(0);
            Long farmId = null;
            String farmerName = null;
            String farmName = null;
            String location = null;
            String cropType = null;
            double hectares = 0;
            int zoneCount = 0;
            Double soilHumidity = null;
            Double temperature = null;
            Double ec = null;
            if (farm != null) {
                farmId = farm.getId();
                farmName = farm.getName();
                location = farm.getLocation();
                cropType = farm.getCropType() != null ? farm.getCropType().name() : null;
                hectares = farm.getHectares();
                var zones = zoneQueryService.findAllByFarmId(farm.getId());
                zoneCount = zones.size();
                if (!zones.isEmpty()) {
                    var readings = telemetryQueryService.getLatestReadingsByZone(
                            new GetLatestReadingsByZoneQuery(zones.get(0).getId()));
                    for (var r : readings) {
                        if (r.getMetricType() == MetricType.SOIL_MOISTURE) soilHumidity = r.getValue();
                        else if (r.getMetricType() == MetricType.SOIL_TEMPERATURE
                                || r.getMetricType() == MetricType.AMBIENT_TEMPERATURE) temperature = r.getValue();
                        else if (r.getMetricType() == MetricType.ELECTRICAL_CONDUCTIVITY) ec = r.getValue();
                    }
                }
                var farmer = userQueryService.handle(new GetUserByIdQuery(client.getFarmerId())).orElse(null);
                if (farmer != null) farmerName = farmer.getFullName();
            }
            return new ClientDetailResource(
                    client.getId(), client.getAgronomistId(), client.getFarmerId(), farmId,
                    farmerName, farmName, location, cropType, hectares, zoneCount,
                    soilHumidity, temperature, ec, client.getActive(), client.getLinkedAt());
        }).toList();
        return ResponseEntity.ok(resources);
    }

    @GetMapping("/agronomist/clients")
    @PreAuthorize("hasRole('AGRONOMIST')")
    public ResponseEntity<List<ClientResource>> getAgronomistClients() {
        Long agronomistId = SecurityContextUtil.getCurrentUserId();
        List<AgronomistClient> clients = agronomistClientQueryService.findByAgronomistId(agronomistId);
        return ResponseEntity.ok(clients.stream().map(ClientResourceFromEntityAssembler::toResourceFromEntity).toList());
    }

    @PostMapping("/agronomist/clients")
    @PreAuthorize("hasRole('AGRONOMIST')")
    public ResponseEntity<?> linkClient(@RequestBody LinkFarmerResource resource) {
        Long agronomistId = SecurityContextUtil.getCurrentUserId();
        var result = agronomistClientCommandService.linkClient(
                new LinkClientCommand(agronomistId, resource.farmerId(), resource.notes()));
        return ResponseEntityAssembler.toResponseEntityFromResult(result,
                ClientResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @DeleteMapping("/agronomist/clients/{clientId}")
    @PreAuthorize("hasRole('AGRONOMIST')")
    public ResponseEntity<?> unlinkClient(@PathVariable Long clientId) {
        Long agronomistId = SecurityContextUtil.getCurrentUserId();
        var result = agronomistClientCommandService.unlinkClient(new UnlinkClientCommand(clientId, agronomistId));
        if (result.isSuccess()) return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/agronomist/summary")
    @PreAuthorize("hasRole('AGRONOMIST')")
    public ResponseEntity<AgronomistSummaryResource> getAgronomistSummary() {
        Long agronomistId = SecurityContextUtil.getCurrentUserId();
        int totalClients = agronomistClientQueryService.countTotalClients(agronomistId);
        int activeClients = agronomistClientQueryService.countActiveClients(agronomistId);
        long totalRecommendations = agronomistClientQueryService.countRecommendations(agronomistId);
        return ResponseEntity.ok(AgronomistSummaryResourceAssembler.toResource(totalClients, activeClients, totalRecommendations));
    }

    @GetMapping("/farmers/{farmerId}/agronomist")
    public ResponseEntity<ClientResource> getAgronomistForFarmer(@PathVariable Long farmerId) {
        return agronomistClientQueryService.findByFarmerId(farmerId)
                .map(c -> ResponseEntity.ok(ClientResourceFromEntityAssembler.toResourceFromEntity(c)))
                .orElseThrow(() -> new ClientLinkNotFoundException("No agronomist linked to farmer with ID: " + farmerId));
    }
}
