package com.satecho.agrosafe.platform.apiservice.edge.interfaces.rest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.satecho.agrosafe.platform.apiservice.edge.application.services.DemoSharedDeviceLinkService;
import com.satecho.agrosafe.platform.apiservice.iot.application.commandservices.DeviceCommandService;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.aggregates.Device;
import com.satecho.agrosafe.platform.apiservice.iot.domain.repositories.DeviceRepository;
import com.satecho.agrosafe.platform.apiservice.irrigation.application.commandservices.ActuatorCommandService;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.aggregates.ActuatorLog;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.commands.LogActuatorCommand;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.valueobjects.ActuatorAction;
import com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.valueobjects.ActuatorType;
import com.satecho.agrosafe.platform.apiservice.irrigation.interfaces.rest.resources.ActuatorLogResource;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.commandservices.ZoneCommandService;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.FarmQueryService;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.ZoneQueryService;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.Farm;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.IrrigationZone;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.commands.LinkDeviceToZoneCommand;
import com.satecho.agrosafe.platform.apiservice.security.application.commandservices.SecurityCommandService;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.commands.IngestSecurityEventCommand;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.valueobjects.EventClassification;
import com.satecho.agrosafe.platform.apiservice.security.interfaces.rest.transform.SecurityEventResourceFromEntityAssembler;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.security.SecurityContextUtil;
import com.satecho.agrosafe.platform.apiservice.shared.interfaces.rest.resources.ErrorResource;
import com.satecho.agrosafe.platform.apiservice.shared.interfaces.rest.transform.ResponseEntityAssembler;
import com.satecho.agrosafe.platform.apiservice.soil.application.commandservices.TelemetryCommandService;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.commands.BatchIngestCommand;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.commands.IngestTelemetryCommand;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.valueobjects.MetricType;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/v1/edge", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Edge", description = "REST ingestion endpoints for edge devices")
public class EdgeController {

    private static final String EDGE_API_KEY_HEADER = "X-Device-Api-Key";

    private final TelemetryCommandService telemetryCommandService;
    private final SecurityCommandService securityCommandService;
    private final ActuatorCommandService actuatorCommandService;
    private final DeviceCommandService deviceCommandService;
    private final DeviceRepository deviceRepository;
    private final FarmQueryService farmQueryService;
    private final ZoneQueryService zoneQueryService;
    private final ZoneCommandService zoneCommandService;
    private final DemoSharedDeviceLinkService demoSharedDeviceLinkService;
    private final ObjectMapper objectMapper;
    private final String edgeApiKey;

    public EdgeController(TelemetryCommandService telemetryCommandService,
                          SecurityCommandService securityCommandService,
                          ActuatorCommandService actuatorCommandService,
                          DeviceCommandService deviceCommandService,
                          DeviceRepository deviceRepository,
                          FarmQueryService farmQueryService,
                          ZoneQueryService zoneQueryService,
                          ZoneCommandService zoneCommandService,
                          DemoSharedDeviceLinkService demoSharedDeviceLinkService,
                          ObjectMapper objectMapper,
                          @Value("${mqtt.edge.api-key}") String edgeApiKey) {
        this.telemetryCommandService = telemetryCommandService;
        this.securityCommandService = securityCommandService;
        this.actuatorCommandService = actuatorCommandService;
        this.deviceCommandService = deviceCommandService;
        this.deviceRepository = deviceRepository;
        this.farmQueryService = farmQueryService;
        this.zoneQueryService = zoneQueryService;
        this.zoneCommandService = zoneCommandService;
        this.demoSharedDeviceLinkService = demoSharedDeviceLinkService;
        this.objectMapper = objectMapper;
        this.edgeApiKey = edgeApiKey;
    }

    @PostMapping(value = "/devices/{deviceId}/soil-readings", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> ingestSoilReadings(@PathVariable Long deviceId,
                                                @RequestHeader(value = EDGE_API_KEY_HEADER, required = false) String apiKeyHeader,
                                                @RequestBody EdgeSoilReadingResource resource) {
        if (!hasValidApiKey(apiKeyHeader, resource.apiKey())) return invalidApiKey();
        var validationError = validateDeviceZoneConsistency(deviceId, resource.zoneId(), true);
        if (validationError != null) return validationError;

        var timestamp = parseTimestamp(
                resource.createdAt() != null ? resource.createdAt() : resource.recordedAt(),
                Instant.now());
        var commands = new ArrayList<IngestTelemetryCommand>();
        if (resource.moisture() != null) {
            commands.add(new IngestTelemetryCommand(deviceId, resource.zoneId(), MetricType.SOIL_MOISTURE, resource.moisture(), timestamp));
        }
        if (resource.ec() != null) {
            commands.add(new IngestTelemetryCommand(deviceId, resource.zoneId(), MetricType.ELECTRICAL_CONDUCTIVITY, resource.ec(), timestamp));
        }
        if (resource.ph() != null) {
            commands.add(new IngestTelemetryCommand(deviceId, resource.zoneId(), MetricType.SOIL_PH, resource.ph(), timestamp));
        }
        if (resource.temperature() != null) {
            commands.add(new IngestTelemetryCommand(deviceId, resource.zoneId(), MetricType.SOIL_TEMPERATURE, resource.temperature(), timestamp));
        }
        if (resource.ambientTemperature() != null) {
            commands.add(new IngestTelemetryCommand(deviceId, resource.zoneId(), MetricType.AMBIENT_TEMPERATURE, resource.ambientTemperature(), timestamp));
        }
        if (commands.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResource("VALIDATION_ERROR", "At least one soil metric is required"));
        }

        var result = telemetryCommandService.batchIngest(new BatchIngestCommand(commands));
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                ingested -> new EdgeIngestSummaryResource(ingested, timestamp),
                HttpStatus.CREATED);
    }

    @PostMapping(value = "/farms/{farmId}/devices/{deviceId}/security-events", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> ingestSecurityEvent(@PathVariable Long farmId,
                                                 @PathVariable Long deviceId,
                                                 @RequestHeader(value = EDGE_API_KEY_HEADER, required = false) String apiKeyHeader,
                                                 @RequestBody EdgeSecurityEventResource resource) {
        if (!hasValidApiKey(apiKeyHeader, resource.apiKey())) return invalidApiKey();
        var validationError = validateFarmDeviceZoneConsistency(farmId, deviceId, resource.zoneId());
        if (validationError != null) return validationError;

        var classification = parseClassification(resource.classification());
        var detectedAt = parseTimestamp(resource.recordedAt(), Instant.now());
        var confidence = resource.confidenceLevel() != null
                ? resource.confidenceLevel()
                : deriveConfidence(classification);
        var location = resource.locationDescription() != null && !resource.locationDescription().isBlank()
                ? resource.locationDescription()
                : resource.zoneId() != null ? "Zone " + resource.zoneId() : "Device " + deviceId;

        var command = new IngestSecurityEventCommand(
                farmId,
                deviceId,
                resource.zoneId(),
                classification,
                confidence,
                detectedAt,
                location,
                toRawDataWithoutApiKey(resource),
                resource.pulseDurationMs(),
                resource.triggersPerMinute());

        var result = securityCommandService.ingestSecurityEvent(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                SecurityEventResourceFromEntityAssembler::toResource,
                HttpStatus.CREATED);
    }

    @PostMapping(value = "/devices/{deviceId}/actuator-logs", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> logActuatorAction(@PathVariable Long deviceId,
                                               @RequestHeader(value = EDGE_API_KEY_HEADER, required = false) String apiKeyHeader,
                                               @RequestBody EdgeActuatorLogResource resource) {
        if (!hasValidApiKey(apiKeyHeader, resource.apiKey())) return invalidApiKey();
        var validationError = validateDeviceZoneConsistency(deviceId, resource.zoneId(), false);
        if (validationError != null) return validationError;

        ActuatorType actuatorType;
        ActuatorAction action;
        try {
            actuatorType = parseActuatorType(resource.actuatorType());
            action = parseActuatorAction(resource.action());
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResource("VALIDATION_ERROR", ex.getMessage()));
        }

        var command = new LogActuatorCommand(
                deviceId,
                resource.zoneId(),
                actuatorType,
                action,
                resource.commandSource() != null ? resource.commandSource() : "EDGE_REST",
                resource.success(),
                resource.responseMessage(),
                parseTimestamp(resource.executedAt(), Instant.now()));

        var result = actuatorCommandService.logActuatorAction(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                EdgeController::toActuatorLogResource,
                HttpStatus.CREATED);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/devices/claim", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> claimDevice(@RequestBody EdgeDeviceClaimResource resource) {
        var currentUserId = SecurityContextUtil.getCurrentUserId();
        if (resource.serialNumber() == null || resource.serialNumber().isBlank()) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResource("VALIDATION_ERROR", "serial_number is required"));
        }
        var farm = farmQueryService.findById(resource.farmId());
        if (farm.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResource("FARM_NOT_FOUND", "Farm not found"));
        }
        if (!farm.get().getUserId().equals(currentUserId) && !SecurityContextUtil.isAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResource("FARM_NOT_OWNED", "The farm does not belong to the authenticated user"));
        }
        var zone = zoneQueryService.findById(resource.zoneId());
        if (zone.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResource("ZONE_NOT_FOUND", "Zone not found"));
        }
        if (!zone.get().getFarmId().equals(resource.farmId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResource("ZONE_FARM_MISMATCH", "The zone does not belong to the selected farm"));
        }
        var device = deviceRepository.findBySerialNumber(resource.serialNumber());
        if (device.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResource("DEVICE_NOT_FOUND", "Device serial number was not found"));
        }
        var existingDevice = device.get();
        if (!canClaimDevice(existingDevice, currentUserId, resource.deviceCode())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResource("DEVICE_CLAIM_FORBIDDEN", "Device code is invalid or the device belongs to another user"));
        }

        existingDevice.setUserId(currentUserId);
        var savedDevice = deviceRepository.save(existingDevice);
        var linkedZone = zone.get();
        if (Boolean.TRUE.equals(resource.linkToZone()) || resource.linkToZone() == null) {
            var linkResult = zoneCommandService.linkDevice(new LinkDeviceToZoneCommand(linkedZone.getId(), savedDevice.getId()));
            if (linkResult.isSuccess()) {
                linkedZone = linkResult.toOptional().orElse(linkedZone);
            }
        }

        return ResponseEntity.ok(new EdgeDeviceClaimResponseResource(
                savedDevice.getId(),
                savedDevice.getSerialNumber(),
                currentUserId,
                farm.get().getId(),
                linkedZone.getId(),
                linkedZone.getDeviceId()));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/devices/claim-demo", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> claimDemoDevice(@RequestBody EdgeDemoDeviceClaimResource resource) {
        var currentUserId = SecurityContextUtil.getCurrentUserId();
        if (resource.farmId() == null || resource.zoneId() == null) {
            return ResponseEntity.badRequest()
                    .body(new ErrorResource("VALIDATION_ERROR", "farm_id and zone_id are required"));
        }
        try {
            var link = demoSharedDeviceLinkService.claim(currentUserId, resource.farmId(), resource.zoneId());
            return ResponseEntity.ok(new EdgeDemoDeviceClaimResponseResource(
                    link.getUserId(),
                    link.getFarmId(),
                    link.getZoneId(),
                    link.getPhysicalDeviceId(),
                    link.getSerialNumber(),
                    link.getActive()));
        } catch (SecurityException ex) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResource("FARM_NOT_OWNED", ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResource("DEMO_DEVICE_CLAIM_INVALID", ex.getMessage()));
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body(new ErrorResource("DEMO_DEVICE_UNAVAILABLE", ex.getMessage()));
        }
    }

    @PostMapping(value = "/devices/{deviceId}/heartbeat", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> recordHeartbeat(@PathVariable Long deviceId,
                                             @RequestHeader(value = EDGE_API_KEY_HEADER, required = false) String apiKeyHeader,
                                             @RequestBody EdgeHeartbeatResource resource) {
        if (!hasValidApiKey(apiKeyHeader, resource.apiKey())) return invalidApiKey();

        var result = deviceCommandService.recordHeartbeat(deviceId, resource.batteryLevel());
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                ignored -> new EdgeIngestSummaryResource(1, Instant.now()),
                HttpStatus.OK);
    }

    private boolean hasValidApiKey(String headerApiKey, String bodyApiKey) {
        return edgeApiKey != null
                && !edgeApiKey.isBlank()
                && (edgeApiKey.equals(headerApiKey) || edgeApiKey.equals(bodyApiKey));
    }

    private ResponseEntity<ErrorResource> invalidApiKey() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResource("INVALID_EDGE_API_KEY", "Invalid edge API key"));
    }

    private ResponseEntity<ErrorResource> validateDeviceZoneConsistency(Long deviceId, Long zoneId, boolean requireZoneLinkedDevice) {
        var device = deviceRepository.findById(deviceId);
        if (device.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResource("DEVICE_NOT_FOUND", "Device not found"));
        }
        if (zoneId == null) return null;
        var zone = zoneQueryService.findById(zoneId);
        if (zone.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResource("ZONE_NOT_FOUND", "Zone not found"));
        }
        var farm = farmQueryService.findById(zone.get().getFarmId());
        if (farm.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResource("FARM_NOT_FOUND", "Farm not found"));
        }
        if (!farm.get().isActive()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResource("FARM_INACTIVE", "This farm is inactive and does not accept edge data"));
        }
        boolean demoPhysicalDevice = demoSharedDeviceLinkService.isPhysicalDemoDevice(deviceId);
        if (!device.get().getUserId().equals(farm.get().getUserId()) && !demoPhysicalDevice) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResource("DEVICE_ZONE_OWNER_MISMATCH", "Device and zone belong to different users"));
        }
        if (requireZoneLinkedDevice && !demoPhysicalDevice && zone.get().getDeviceId() != null && !zone.get().getDeviceId().equals(deviceId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResource("DEVICE_NOT_LINKED_TO_ZONE", "The zone is linked to a different device"));
        }
        return null;
    }

    private ResponseEntity<ErrorResource> validateFarmDeviceZoneConsistency(Long farmId, Long deviceId, Long zoneId) {
        var farm = farmQueryService.findById(farmId);
        if (farm.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResource("FARM_NOT_FOUND", "Farm not found"));
        }
        if (!farm.get().isActive()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ErrorResource("FARM_INACTIVE", "This farm is inactive and does not accept edge data"));
        }
        var device = deviceRepository.findById(deviceId);
        if (device.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResource("DEVICE_NOT_FOUND", "Device not found"));
        }
        boolean demoPhysicalDevice = demoSharedDeviceLinkService.isPhysicalDemoDevice(deviceId);
        if (!device.get().getUserId().equals(farm.get().getUserId()) && !demoPhysicalDevice) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ErrorResource("DEVICE_FARM_OWNER_MISMATCH", "Device and farm belong to different users"));
        }
        if (zoneId != null) {
            var zone = zoneQueryService.findById(zoneId);
            if (zone.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResource("ZONE_NOT_FOUND", "Zone not found"));
            }
            if (!zone.get().getFarmId().equals(farmId)) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(new ErrorResource("ZONE_FARM_MISMATCH", "The zone does not belong to the farm"));
            }
        }
        return null;
    }

    private boolean canClaimDevice(Device device, Long currentUserId, String deviceCode) {
        if (SecurityContextUtil.isAdmin()) return true;
        if (device.getUserId() != null && device.getUserId().equals(currentUserId)) return true;
        if (deviceCode == null || deviceCode.isBlank()) return false;
        return device.getCredentials() != null && deviceCode.equals(device.getCredentials().apiKey());
    }

    private static Instant parseTimestamp(String raw, Instant fallback) {
        if (raw == null || raw.isBlank()) return fallback;
        try {
            return ZonedDateTime.parse(raw).toInstant();
        } catch (Exception ignored) {
            try {
                return Instant.parse(raw);
            } catch (Exception ignoredAgain) {
                return fallback;
            }
        }
    }

    private static EventClassification parseClassification(String raw) {
        if (raw == null || raw.isBlank()) return EventClassification.UNKNOWN;
        try {
            return EventClassification.valueOf(raw.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return EventClassification.UNKNOWN;
        }
    }

    private static double deriveConfidence(EventClassification classification) {
        return switch (classification) {
            case PERSON -> 90.0;
            case ANIMAL -> 70.0;
            case WIND -> 55.0;
            case UNKNOWN -> 50.0;
        };
    }

    private static ActuatorType parseActuatorType(String raw) {
        if (raw == null || raw.isBlank()) return ActuatorType.VALVE;
        try {
            return ActuatorType.valueOf(raw.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid actuator_type. Allowed values: VALVE, PUMP, RELAY, SOLENOID");
        }
    }

    private static ActuatorAction parseActuatorAction(String raw) {
        if (raw == null || raw.isBlank()) return ActuatorAction.TOGGLE;
        try {
            return ActuatorAction.valueOf(raw.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid action. Allowed values: IRRIGATE_ON, IRRIGATE_OFF, OPEN, CLOSE, TOGGLE");
        }
    }

    private String toRawDataWithoutApiKey(EdgeSecurityEventResource resource) {
        try {
            Map<String, Object> raw = new LinkedHashMap<>();
            raw.put("zone_id", resource.zoneId());
            raw.put("pulse_duration_ms", resource.pulseDurationMs());
            raw.put("triggers_per_minute", resource.triggersPerMinute());
            raw.put("classification", resource.classification());
            raw.put("confidence_level", resource.confidenceLevel());
            raw.put("recorded_at", resource.recordedAt());
            raw.put("location_description", resource.locationDescription());
            return objectMapper.writeValueAsString(raw);
        } catch (Exception ignored) {
            return "{}";
        }
    }

    private static ActuatorLogResource toActuatorLogResource(ActuatorLog log) {
        return new ActuatorLogResource(
                log.getId(),
                log.getDeviceId(),
                log.getZoneId(),
                log.getActuatorType().name(),
                log.getAction().name(),
                log.getCommandSource(),
                log.getExecutedAt(),
                log.isSuccess(),
                log.getResponseMessage());
    }

    public record EdgeSoilReadingResource(
            @JsonProperty("api_key") String apiKey,
            @JsonProperty("zone_id") Long zoneId,
            Double moisture,
            Double ec,
            Double ph,
            Double temperature,
            @JsonProperty("ambient_temperature") Double ambientTemperature,
            @JsonProperty("created_at") String createdAt,
            @JsonProperty("recorded_at") String recordedAt
    ) {
    }

    public record EdgeSecurityEventResource(
            @JsonProperty("api_key") String apiKey,
            @JsonProperty("zone_id") Long zoneId,
            @JsonProperty("pulse_duration_ms") Double pulseDurationMs,
            @JsonProperty("triggers_per_minute") Integer triggersPerMinute,
            String classification,
            @JsonProperty("confidence_level") Double confidenceLevel,
            @JsonProperty("recorded_at") String recordedAt,
            @JsonProperty("location_description") String locationDescription
    ) {
    }

    public record EdgeActuatorLogResource(
            @JsonProperty("api_key") String apiKey,
            @JsonProperty("zone_id") Long zoneId,
            @JsonProperty("actuator_type") String actuatorType,
            String action,
            @JsonProperty("command_source") String commandSource,
            boolean success,
            @JsonProperty("response_message") String responseMessage,
            @JsonProperty("executed_at") String executedAt
    ) {
    }

    public record EdgeHeartbeatResource(
            @JsonProperty("api_key") String apiKey,
            @JsonProperty("battery_level") Double batteryLevel
    ) {
    }

    public record EdgeIngestSummaryResource(int ingested, Instant ingestedAt) {
    }

    public record EdgeDeviceClaimResource(
            @JsonProperty("serial_number") String serialNumber,
            @JsonProperty("device_code") String deviceCode,
            @JsonProperty("farm_id") Long farmId,
            @JsonProperty("zone_id") Long zoneId,
            @JsonProperty("link_to_zone") Boolean linkToZone
    ) {
    }

    public record EdgeDeviceClaimResponseResource(
            Long deviceId,
            String serialNumber,
            Long userId,
            Long farmId,
            Long zoneId,
            Long linkedZoneDeviceId
    ) {
    }

    public record EdgeDemoDeviceClaimResource(
            @JsonProperty("farm_id") Long farmId,
            @JsonProperty("zone_id") Long zoneId
    ) {
    }

    public record EdgeDemoDeviceClaimResponseResource(
            Long userId,
            Long farmId,
            Long zoneId,
            Long physicalDeviceId,
            String serialNumber,
            Boolean active
    ) {
    }
}
