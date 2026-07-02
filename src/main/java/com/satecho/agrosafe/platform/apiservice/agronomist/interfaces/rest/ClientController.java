package com.satecho.agrosafe.platform.apiservice.agronomist.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.agronomist.application.commandservices.ClientCommandService;
import com.satecho.agrosafe.platform.apiservice.agronomist.application.queryservices.ClientQueryService;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.commands.AssignClientCommand;
import com.satecho.agrosafe.platform.apiservice.agronomist.interfaces.rest.resources.AssignClientResource;
import com.satecho.agrosafe.platform.apiservice.agronomist.interfaces.rest.resources.ClientDetailResource;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.security.SecurityContextUtil;
import com.satecho.agrosafe.platform.apiservice.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Agronomist Clients", description = "Client roster management for agronomists (EP-009)")
public class ClientController {

    private final ClientQueryService clientQueryService;
    private final ClientCommandService clientCommandService;

    public ClientController(ClientQueryService clientQueryService, ClientCommandService clientCommandService) {
        this.clientQueryService = clientQueryService;
        this.clientCommandService = clientCommandService;
    }

    @PreAuthorize("hasRole('AGRONOMIST')")
    @GetMapping("/agronomist/clients/detailed")
    public ResponseEntity<List<ClientDetailResource>> getClientsDetailed() {
        Long agronomistUserId = SecurityContextUtil.getCurrentUserId();
        var details = clientQueryService.findAssignedFarmersDetailed(agronomistUserId).stream()
                .map(d -> new ClientDetailResource(d.id(), d.agronomistUserId(), d.farmerUserId(), d.farmId(),
                        d.farmerName(), d.farmName(), d.location(), d.cropType(), d.hectares(), d.zoneCount(),
                        d.soilHumidity(), d.temperature(), d.ec(), d.active()))
                .toList();
        return ResponseEntity.ok(details);
    }

    @PreAuthorize("hasRole('AGRONOMIST')")
    @PostMapping("/agronomist/clients")
    public ResponseEntity<?> assignClient(@RequestBody AssignClientResource resource) {
        Long agronomistUserId = SecurityContextUtil.getCurrentUserId();
        var command = new AssignClientCommand(agronomistUserId, resource.farmerId());
        var result = clientCommandService.assignClient(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, a -> Map.of("id", a.getId(), "farmerId", a.getFarmerUserId()), HttpStatus.CREATED);
    }

    /** A farmer checking which agronomist (if any) currently advises them. */
    @GetMapping("/farmers/{farmerId}/agronomist")
    public ResponseEntity<?> getAssignedAgronomist(@PathVariable Long farmerId) {
        Long currentUserId = SecurityContextUtil.getCurrentUserId();
        if (!currentUserId.equals(farmerId) && !SecurityContextUtil.isAdmin()) {
            return ResponseEntity.status(403).build();
        }
        Long agronomistUserId = clientQueryService.findAssignedAgronomistUserId(farmerId);
        if (agronomistUserId == null) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(Map.of("agronomistId", agronomistUserId));
    }
}
