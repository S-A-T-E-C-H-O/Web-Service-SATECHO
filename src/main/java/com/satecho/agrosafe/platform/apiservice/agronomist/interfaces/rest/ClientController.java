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

/**
 * Controller to handle operations related to agronomist clients, such as retrieving assigned clients,
 * assigning a client to an agronomist, and finding the assigned agronomist for a farmer.
 */
@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Agronomist Clients", description = "Client roster management for agronomists (EP-009)")
public class ClientController {

    /**
     * Service to handle queries related to clients.
     */
    private final ClientQueryService clientQueryService;

    /**
     * Service to handle commands related to clients.
     */
    private final ClientCommandService clientCommandService;

    /**
     * Constructs a new ClientController with the required query and command services.
     *
     * @param clientQueryService the query service for client operations
     * @param clientCommandService the command service for client operations
     */
    public ClientController(ClientQueryService clientQueryService, ClientCommandService clientCommandService) {
        this.clientQueryService = clientQueryService;
        this.clientCommandService = clientCommandService;
    }

    /**
     * Retrieves detailed information of all clients assigned to the currently logged-in agronomist.
     *
     * @return a response entity containing a list of client detail resources
     */
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

    /**
     * Assigns a client (farmer) to the currently logged-in agronomist.
     *
     * @param resource the resource representing the client assignment request
     * @return a response entity with the assignment result status and details
     */
    @PreAuthorize("hasRole('AGRONOMIST')")
    @PostMapping("/agronomist/clients")
    public ResponseEntity<?> assignClient(@RequestBody AssignClientResource resource) {
        Long agronomistUserId = SecurityContextUtil.getCurrentUserId();
        var command = new AssignClientCommand(agronomistUserId, resource.farmerId());
        var result = clientCommandService.assignClient(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, a -> Map.of("id", a.getId(), "farmerId", a.getFarmerUserId()), HttpStatus.CREATED);
    }

    /**
     * Retrieves the agronomist assigned to a specific farmer.
     * A farmer checking which agronomist (if any) currently advises them.
     *
     * @param farmerId the unique identifier of the farmer
     * @return a response entity containing the assigned agronomist's user ID
     */
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

