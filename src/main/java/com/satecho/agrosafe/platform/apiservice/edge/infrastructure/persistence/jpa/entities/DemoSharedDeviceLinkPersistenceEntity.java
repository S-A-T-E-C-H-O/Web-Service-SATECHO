package com.satecho.agrosafe.platform.apiservice.edge.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_demo_device_links")
@Getter
@Setter
@NoArgsConstructor
public class DemoSharedDeviceLinkPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "farm_id", nullable = false)
    private Long farmId;

    @Column(name = "zone_id", nullable = false)
    private Long zoneId;

    @Column(name = "physical_device_id", nullable = false)
    private Long physicalDeviceId;

    @Column(name = "serial_number", nullable = false, length = 100)
    private String serialNumber;

    @Column(name = "active", nullable = false)
    private Boolean active = true;
}
