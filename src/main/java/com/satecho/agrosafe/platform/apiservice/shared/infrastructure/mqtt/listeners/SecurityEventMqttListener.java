package com.satecho.agrosafe.platform.apiservice.shared.infrastructure.mqtt.listeners;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.satecho.agrosafe.platform.apiservice.security.application.commandservices.SecurityCommandService;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.commands.IngestSecurityEventCommand;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.valueobjects.EventClassification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * Receives PIR events from the edge via MQTT and persists them via SecurityCommandService.
 *
 * Edge payload (topic: agrosafe/{farmId}/devices/{deviceId}/security/event):
 * { "api_key": "...", "zone_id": 1, "pulse_duration_ms": 2500,
 *   "triggers_per_minute": 2, "classification": "PERSON", "recorded_at": "..." }
 *
 * Mapping to SecurityEvent:
 *   classification   → EventClassification (WIND/ANIMAL/PERSON match directly)
 *   confidenceLevel  → derived from classification (PERSON=90, ANIMAL=70, WIND=55)
 *   locationDescription → "Zone {zoneId}" or "Device {deviceId}"
 *   rawData          → full JSON payload string
 */
@Component
public class SecurityEventMqttListener {

    private static final Logger log = LoggerFactory.getLogger(SecurityEventMqttListener.class);

    private final SecurityCommandService securityCommandService;
    private final ObjectMapper objectMapper;

    @Value("${mqtt.edge.api-key}")
    private String edgeApiKey;

    public SecurityEventMqttListener(SecurityCommandService securityCommandService, ObjectMapper objectMapper) {
        this.securityCommandService = securityCommandService;
        this.objectMapper = objectMapper;
    }

    @ServiceActivator(inputChannel = "mqttSecurityInputChannel")
    public void handleSecurityEvent(Message<String> message) {
        String topic   = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
        String payload = message.getPayload();
        try {
            PirPayloadDto dto = objectMapper.readValue(payload, PirPayloadDto.class);

            if (!edgeApiKey.equals(dto.apiKey())) {
                log.warn("Rejected security event on topic {} — invalid api_key", topic);
                return;
            }

            // topic: agrosafe/{farmId}/devices/{deviceId}/security/event
            String[] parts   = topic.split("/");
            Long     farmId  = Long.parseLong(parts[1]);
            Long     deviceId = Long.parseLong(parts[3]);

            EventClassification classification = parseClassification(dto.classification());
            double confidenceLevel = dto.confidenceLevel() != null ? dto.confidenceLevel() : deriveConfidence(classification);
            Instant detectedAt = parseTimestamp(dto.recordedAt());
            String location = dto.locationDescription() != null && !dto.locationDescription().isBlank()
                    ? dto.locationDescription()
                    : dto.zoneId() != null ? "Zone " + dto.zoneId() : "Device " + deviceId;

            var command = new IngestSecurityEventCommand(farmId, deviceId, dto.zoneId(), classification,
                    confidenceLevel, detectedAt, location, payload,
                    dto.pulseDurationMs(), dto.triggersPerMinute());

            var result = securityCommandService.ingestSecurityEvent(command);
            if (result.isFailure()) {
                log.warn("Failed to persist security event from topic {}", topic);
            } else {
                log.info("Security event persisted — farmId={} deviceId={} classification={} severity={}",
                        farmId, deviceId, classification, result.toOptional().map(e -> e.getSeverity().name()).orElse("?"));
            }

        } catch (Exception e) {
            log.error("Error processing security MQTT message on topic {}: {}", topic, e.getMessage(), e);
        }
    }

    private EventClassification parseClassification(String raw) {
        if (raw == null) return EventClassification.UNKNOWN;
        try { return EventClassification.valueOf(raw.toUpperCase()); }
        catch (IllegalArgumentException ex) { return EventClassification.UNKNOWN; }
    }

    private double deriveConfidence(EventClassification classification) {
        return switch (classification) {
            case PERSON  -> 90.0;
            case ANIMAL  -> 70.0;
            case WIND    -> 55.0;
            case UNKNOWN -> 50.0;
        };
    }

    private Instant parseTimestamp(String recordedAt) {
        if (recordedAt == null || recordedAt.isBlank()) return Instant.now();
        try { return ZonedDateTime.parse(recordedAt).toInstant(); }
        catch (Exception e1) {
            try { return Instant.parse(recordedAt); }
            catch (Exception e2) { return Instant.now(); }
        }
    }

    private record PirPayloadDto(
            @JsonProperty("api_key")             String  apiKey,
            @JsonProperty("zone_id")             Long    zoneId,
            @JsonProperty("pulse_duration_ms")   Double  pulseDurationMs,
            @JsonProperty("triggers_per_minute") Integer triggersPerMinute,
            String classification,
            @JsonProperty("confidence_level")    Double  confidenceLevel,
            @JsonProperty("location_description") String locationDescription,
            @JsonProperty("recorded_at")         String  recordedAt
    ) {}
}
