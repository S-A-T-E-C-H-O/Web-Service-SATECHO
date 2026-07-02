package com.satecho.agrosafe.platform.apiservice.shared.infrastructure.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Publica comandos de actuador (válvula, bomba) hacia dispositivos vía MQTT.
 *
 * Topic: agrosafe/{farmId}/devices/{deviceId}/actuator/command
 * Payload: {"action":"IRRIGATE_ON","duration_minutes":30} | {"action":"IRRIGATE_OFF"}
 *
 * Valores de action alineados con:
 *   - ESP32: mqttCommandTask()               (IRRIGATE_ON / IRRIGATE_OFF, OPEN/CLOSE como alias)
 *   - Edge:  actuator_command_subscriber.py  (pasa el payload sin traducir; buferea si offline)
 */
@Component
public class MqttActuatorPublisher {

    private static final Logger log = LoggerFactory.getLogger(MqttActuatorPublisher.class);
    private static final String TOPIC_TEMPLATE = "agrosafe/%d/devices/%d/actuator/command";

    private final MqttGateway gateway;
    private final ObjectMapper objectMapper;

    public MqttActuatorPublisher(MqttGateway gateway, ObjectMapper objectMapper) {
        this.gateway = gateway;
        this.objectMapper = objectMapper;
    }

    public void publishOpen(Long farmId, Long deviceId, Integer durationMinutes) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("action", "IRRIGATE_ON");
        if (durationMinutes != null) payload.put("duration_minutes", durationMinutes);
        publish(farmId, deviceId, payload);
    }

    public void publishClose(Long farmId, Long deviceId) {
        publish(farmId, deviceId, Map.of("action", "IRRIGATE_OFF"));
    }

    private void publish(Long farmId, Long deviceId, Map<String, Object> payload) {
        try {
            String topic = String.format(TOPIC_TEMPLATE, farmId, deviceId);
            String json  = objectMapper.writeValueAsString(payload);
            gateway.publish(topic, json);
            log.info("Actuator command published — topic={} payload={}", topic, json);
        } catch (Exception e) {
            log.warn("Failed to publish actuator command to device {}: {}", deviceId, e.getMessage());
        }
    }
}
