package com.satecho.agrosafe.platform.analytics.application;

import com.satecho.agrosafe.platform.apiservice.analytics.application.internal.queryservices.ParcelComparisonQueryServiceImpl;
import com.satecho.agrosafe.platform.apiservice.onboarding.application.queryservices.ZoneQueryService;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.IrrigationZone;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.valueobjects.CropType;
import com.satecho.agrosafe.platform.apiservice.soil.application.queryservices.TelemetryQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ParcelComparisonQueryServiceImpl")
class ParcelComparisonQueryServiceImplTest {

    @Mock ZoneQueryService zoneQueryService;
    @Mock TelemetryQueryService telemetryQueryService;
    @InjectMocks ParcelComparisonQueryServiceImpl service;

    @Test
    @DisplayName("compare: empty zone list returns failure")
    void compare_emptyList_returnsFailure() {
        var result = service.compare(List.of());
        assertThat(result.isFailure()).isTrue();
    }

    @Test
    @DisplayName("compare: more than 4 zones returns failure")
    void compare_moreThanFour_returnsFailure() {
        var result = service.compare(List.of(1L, 2L, 3L, 4L, 5L));
        assertThat(result.isFailure()).isTrue();
    }

    @Test
    @DisplayName("compare: unknown zone returns failure")
    void compare_unknownZone_returnsFailure() {
        when(zoneQueryService.findById(1L)).thenReturn(Optional.empty());
        var result = service.compare(List.of(1L));
        assertThat(result.isFailure()).isTrue();
    }

    @Test
    @DisplayName("compare: valid zones returns a comparison entry per zone")
    void compare_validZones_returnsSuccess() {
        var zone1 = new IrrigationZone(10L, "North", 2.0, CropType.CORN);
        zone1.setId(1L);
        var zone2 = new IrrigationZone(10L, "South", 3.0, CropType.CORN);
        zone2.setId(2L);
        when(zoneQueryService.findById(1L)).thenReturn(Optional.of(zone1));
        when(zoneQueryService.findById(2L)).thenReturn(Optional.of(zone2));
        when(telemetryQueryService.getLatestReadingsByZone(org.mockito.ArgumentMatchers.any())).thenReturn(List.of());

        var result = service.compare(List.of(1L, 2L));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.toOptional().get()).hasSize(2);
    }
}
