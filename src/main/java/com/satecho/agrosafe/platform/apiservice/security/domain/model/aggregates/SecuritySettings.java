package com.satecho.agrosafe.platform.apiservice.security.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.security.domain.model.valueobjects.AlertMode;
import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class SecuritySettings extends AuditableAbstractAggregateRoot<SecuritySettings> {
    @Setter private Long id;
    @Setter private Long farmId;
    @Setter private Integer motionSensitivity;
    @Setter private AlertMode alertMode;
    @Setter private String detectionScheduleStart;
    @Setter private String detectionScheduleEnd;
    @Setter private String notificationContacts;
    @Setter private Set<Long> disabledZoneIds;

    public SecuritySettings() {
        this.disabledZoneIds = new HashSet<>();
    }

    public SecuritySettings(Long farmId) {
        if (farmId == null) throw new IllegalArgumentException("farmId cannot be null");
        this.farmId = farmId;
        this.motionSensitivity = 70;
        this.alertMode = AlertMode.ALL_EVENTS;
        this.detectionScheduleStart = "00:00";
        this.detectionScheduleEnd = "23:59";
        this.notificationContacts = "[]";
        this.disabledZoneIds = new HashSet<>();
    }

    public void update(Integer motionSensitivity, AlertMode alertMode,
                       String detectionScheduleStart, String detectionScheduleEnd, String notificationContacts) {
        if (motionSensitivity != null) {
            if (motionSensitivity < 30 || motionSensitivity > 100) throw new IllegalArgumentException("Motion sensitivity must be between 30 and 100");
            this.motionSensitivity = motionSensitivity;
        }
        if (alertMode != null) this.alertMode = alertMode;
        if (detectionScheduleStart != null) this.detectionScheduleStart = detectionScheduleStart;
        if (detectionScheduleEnd != null) this.detectionScheduleEnd = detectionScheduleEnd;
        if (notificationContacts != null) this.notificationContacts = notificationContacts;
    }

    /** EP-013: a farmer can silence perimeter detection for a specific parcel without disabling the whole farm. */
    public void setZoneDetectionEnabled(Long zoneId, boolean enabled) {
        if (disabledZoneIds == null) disabledZoneIds = new HashSet<>();
        if (enabled) disabledZoneIds.remove(zoneId);
        else disabledZoneIds.add(zoneId);
    }

    public boolean isZoneDetectionEnabled(Long zoneId) {
        return disabledZoneIds == null || zoneId == null || !disabledZoneIds.contains(zoneId);
    }
}
