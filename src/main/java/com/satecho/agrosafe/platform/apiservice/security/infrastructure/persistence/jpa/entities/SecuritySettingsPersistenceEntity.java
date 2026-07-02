package com.satecho.agrosafe.platform.apiservice.security.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.security.domain.model.valueobjects.AlertMode;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "security_settings")
@Getter
@Setter
@NoArgsConstructor
public class SecuritySettingsPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Column(name = "farm_id", nullable = false, unique = true)
    private Long farmId;
    @Column(name = "motion_sensitivity", nullable = false)
    private Integer motionSensitivity;
    @Enumerated(EnumType.STRING)
    @Column(name = "alert_mode", nullable = false, length = 20)
    private AlertMode alertMode;
    @Column(name = "detection_schedule_start", length = 5)
    private String detectionScheduleStart;
    @Column(name = "detection_schedule_end", length = 5)
    private String detectionScheduleEnd;
    @Column(name = "notification_contacts", columnDefinition = "TEXT")
    private String notificationContacts;
    @ElementCollection
    @CollectionTable(name = "security_settings_disabled_zones", joinColumns = @JoinColumn(name = "security_settings_id"))
    @Column(name = "zone_id")
    private java.util.Set<Long> disabledZoneIds = new java.util.HashSet<>();
}
