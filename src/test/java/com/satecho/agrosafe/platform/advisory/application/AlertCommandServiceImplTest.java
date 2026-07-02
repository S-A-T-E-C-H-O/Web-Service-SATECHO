package com.satecho.agrosafe.platform.advisory.application;

import com.satecho.agrosafe.platform.apiservice.advisory.application.internal.commandservices.AlertCommandServiceImpl;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.Alert;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.events.AlertCreatedEvent;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.AlertStatus;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.AlertType;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.repositories.AlertRepository;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.IrrigationZone;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.valueobjects.CropType;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.valueobjects.ThresholdLimits;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.repositories.ZoneRepository;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.events.TelemetryReceivedEvent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AlertCommandServiceImpl")
class AlertCommandServiceImplTest {

    @Mock AlertRepository alertRepository;
    @Mock ZoneRepository zoneRepository;
    @Mock ApplicationEventPublisher eventPublisher;
    @InjectMocks AlertCommandServiceImpl service;

    private IrrigationZone zoneWithThresholds(Long farmId, ThresholdLimits thresholds) {
        IrrigationZone zone = new IrrigationZone(farmId, "North Field", 2.5, CropType.CORN);
        zone.setId(10L);
        zone.updateThresholds(thresholds);
        return zone;
    }

    @Test
    @DisplayName("evaluateReading: moisture below zone minimum creates a MOISTURE_LOW alert and publishes AlertCreatedEvent")
    void evaluateReading_moistureBelowMin_createsAlertAndPublishes() {
        var thresholds = new ThresholdLimits(20.0, 80.0, 0.0, 5.0, 5.5, 7.5, 10.0, 40.0);
        var zone = zoneWithThresholds(1L, thresholds);
        when(zoneRepository.findById(10L)).thenReturn(Optional.of(zone));
        when(alertRepository.findByZoneIdAndAlertTypeAndStatus(10L, AlertType.MOISTURE_LOW, AlertStatus.ACTIVE))
                .thenReturn(Optional.empty());
        when(alertRepository.save(any(Alert.class))).thenAnswer(inv -> {
            Alert a = inv.getArgument(0);
            a.setId(99L);
            return a;
        });

        var event = new TelemetryReceivedEvent(1L, 5L, 10L, "SOIL_MOISTURE", 15.0, Instant.now());
        service.evaluateReading(event);

        verify(alertRepository).save(any(Alert.class));
        verify(eventPublisher).publishEvent(any(AlertCreatedEvent.class));
    }

    @Test
    @DisplayName("evaluateReading: breach with an already-active alert does not create a duplicate")
    void evaluateReading_breachWithActiveAlert_doesNotDuplicate() {
        var thresholds = new ThresholdLimits(20.0, 80.0, 0.0, 5.0, 5.5, 7.5, 10.0, 40.0);
        var zone = zoneWithThresholds(1L, thresholds);
        when(zoneRepository.findById(10L)).thenReturn(Optional.of(zone));
        Alert existingAlert = new Alert(10L, 5L, 1L, AlertType.MOISTURE_LOW,
                com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.AlertSeverity.CRITICAL, 15.0, 20.0);
        when(alertRepository.findByZoneIdAndAlertTypeAndStatus(10L, AlertType.MOISTURE_LOW, AlertStatus.ACTIVE))
                .thenReturn(Optional.of(existingAlert));

        var event = new TelemetryReceivedEvent(1L, 5L, 10L, "SOIL_MOISTURE", 12.0, Instant.now());
        service.evaluateReading(event);

        verify(alertRepository, never()).save(any(Alert.class));
        verifyNoInteractions(eventPublisher);
    }

    @Test
    @DisplayName("evaluateReading: reading back within range resolves the active alert (hysteresis)")
    void evaluateReading_backWithinRange_resolvesActiveAlert() {
        var thresholds = new ThresholdLimits(20.0, 80.0, 0.0, 5.0, 5.5, 7.5, 10.0, 40.0);
        var zone = zoneWithThresholds(1L, thresholds);
        when(zoneRepository.findById(10L)).thenReturn(Optional.of(zone));
        Alert existingAlert = new Alert(10L, 5L, 1L, AlertType.MOISTURE_LOW,
                com.satecho.agrosafe.platform.apiservice.advisory.domain.model.valueobjects.AlertSeverity.CRITICAL, 15.0, 20.0);
        existingAlert.setId(99L);
        when(alertRepository.findByZoneIdAndAlertTypeAndStatus(10L, AlertType.MOISTURE_LOW, AlertStatus.ACTIVE))
                .thenReturn(Optional.of(existingAlert));
        when(alertRepository.save(any(Alert.class))).thenAnswer(inv -> inv.getArgument(0));

        var event = new TelemetryReceivedEvent(1L, 5L, 10L, "SOIL_MOISTURE", 45.0, Instant.now());
        service.evaluateReading(event);

        ArgumentCaptor<Alert> captor = ArgumentCaptor.forClass(Alert.class);
        verify(alertRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(AlertStatus.RESOLVED);
        verify(eventPublisher, never()).publishEvent(any(AlertCreatedEvent.class));
    }

    @Test
    @DisplayName("evaluateReading: metric type without threshold mapping (pH) is ignored")
    void evaluateReading_unmappedMetricType_isIgnored() {
        var event = new TelemetryReceivedEvent(1L, 5L, 10L, "SOIL_PH", 6.0, Instant.now());
        service.evaluateReading(event);

        verifyNoInteractions(zoneRepository, alertRepository, eventPublisher);
    }
}
