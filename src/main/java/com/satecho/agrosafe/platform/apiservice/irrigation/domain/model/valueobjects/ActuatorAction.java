package com.satecho.agrosafe.platform.apiservice.irrigation.domain.model.valueobjects;

public enum ActuatorAction {
    // IRRIGATE_ON/OFF: acciones que publica MqttActuatorPublisher y que el Edge
    // reenvía al log de actuadores (POST /api/v1/actuators/{id}/command).
    // OPEN/CLOSE/TOGGLE: alias genéricos de válvula (compatibilidad).
    IRRIGATE_ON, IRRIGATE_OFF, OPEN, CLOSE, TOGGLE
}
