package com.satecho.agrosafe.platform.apiservice.soil.interfaces.mqtt;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.satecho.agrosafe.platform.apiservice.soil.application.commandservices.TelemetryCommandService;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.commands.BatchIngestCommand;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.commands.IngestTelemetryCommand;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.valueobjects.MetricType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Subscribes to agrosafe/+/devices/+/soil/reading (wired via MqttConfig → mqttInputChannel).
 * Fans out one edge message (4 metrics) into 4 SensorReading rows via batchIngest().
 *
 * Edge payload:
 * { "api_key": "...", "zone_id": 12, "moisture": 45.2, "ec": 1.8,
 *   "ph": 6.4, "temperature": 22.5, "created_at": "2026-06-15T18:23:00-05:00" }
 */
@Component
public class SoilTelemetryMqttListener {

    private static final Logger log = LoggerFactory.getLogger(SoilTelemetryMqttListener.class);

    private final TelemetryCommandService telemetryCommandService;
    private final ObjectMapper objectMapper;

    @Value("${mqtt.edge.api-key}")
    private String edgeApiKey;

    public SoilTelemetryMqttListener(TelemetryCommandService telemetryCommandService, ObjectMapper objectMapper) {
        this.telemetryCommandService = telemetryCommandService;
        this.objectMapper = objectMapper;
    }

    @ServiceActivator(inputChannel = "mqttInputChannel")
    public void handleSoilReading(Message<String> message) {
        String topic   = (String) message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC);
        String payload = message.getPayload();
        try {
            SoilPayloadDto dto = objectMapper.readValue(payload, SoilPayloadDto.class);

            if (!edgeApiKey.equals(dto.apiKey())) {
                log.warn("Rejected MQTT soil message on topic {} — invalid api_key", topic);
                return;
            }

            // topic: agrosafe/{farmId}/devices/{deviceId}/soil/reading
            String[] parts   = topic.split("/");
            Long     deviceId = Long.parseLong(parts[3]);
            Long     zoneId   = dto.zoneId();
            Instant  ts       = parseTimestamp(dto.createdAt());

            List<IngestTelemetryCommand> commands = new ArrayList<>();
            if (dto.moisture()    != null) commands.add(new IngestTelemetryCommand(deviceId, zoneId, MetricType.SOIL_MOISTURE,            dto.moisture(),    ts));
            if (dto.ec()          != null) commands.add(new IngestTelemetryCommand(deviceId, zoneId, MetricType.ELECTRICAL_CONDUCTIVITY,  dto.ec(),          ts));
            if (dto.ph()          != null) commands.add(new IngestTelemetryCommand(deviceId, zoneId, MetricType.SOIL_PH,                  dto.ph(),          ts));
            if (dto.temperature() != null) commands.add(new IngestTelemetryCommand(deviceId, zoneId, MetricType.SOIL_TEMPERATURE,         dto.temperature(), ts));

            if (commands.isEmpty()) {
                log.warn("Soil MQTT message on topic {} had no valid metric fields", topic);
                return;
            }

            var result = telemetryCommandService.batchIngest(new BatchIngestCommand(commands));
            if (result.isFailure()) {
                log.warn("Batch ingest failed for soil MQTT message on topic {}", topic);
            } else {
                log.debug("Ingested {} soil readings from deviceId={} topic={}", commands.size(), deviceId, topic);
            }

        } catch (Exception e) {
            log.error("Error processing MQTT soil message on topic {}: {}", topic, e.getMessage(), e);
        }
    }

    private Instant parseTimestamp(String createdAt) {
        if (createdAt == null || createdAt.isBlank()) return Instant.now();
        try {
            return ZonedDateTime.parse(createdAt).toInstant();
        } catch (Exception e) {
            try {
                return Instant.parse(createdAt);
            } catch (Exception ex) {
                log.warn("Could not parse timestamp '{}', using now", createdAt);
                return Instant.now();
            }
        }
    }

    private record SoilPayloadDto(
            @JsonProperty("api_key")     String  apiKey,
            @JsonProperty("zone_id")     Long    zoneId,
            Double moisture,
            Double ec,
            Double ph,
            Double temperature,
            @JsonProperty("created_at") String  createdAt
    ) {}
}
