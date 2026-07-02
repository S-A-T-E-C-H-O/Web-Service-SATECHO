package com.satecho.agrosafe.platform.security.domain;

import com.satecho.agrosafe.platform.apiservice.security.domain.model.aggregates.SecuritySettings;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("SecuritySettings zone detection toggle (EP-013)")
class SecuritySettingsZoneToggleTest {

    @Test
    @DisplayName("a zone is enabled by default")
    void zoneEnabledByDefault() {
        var settings = new SecuritySettings(1L);
        assertThat(settings.isZoneDetectionEnabled(5L)).isTrue();
    }

    @Test
    @DisplayName("disabling a zone makes isZoneDetectionEnabled return false only for that zone")
    void disablingZone_affectsOnlyThatZone() {
        var settings = new SecuritySettings(1L);
        settings.setZoneDetectionEnabled(5L, false);

        assertThat(settings.isZoneDetectionEnabled(5L)).isFalse();
        assertThat(settings.isZoneDetectionEnabled(6L)).isTrue();
    }

    @Test
    @DisplayName("re-enabling a zone clears the suppression")
    void reEnablingZone_clearsSuppression() {
        var settings = new SecuritySettings(1L);
        settings.setZoneDetectionEnabled(5L, false);
        settings.setZoneDetectionEnabled(5L, true);

        assertThat(settings.isZoneDetectionEnabled(5L)).isTrue();
    }
}
