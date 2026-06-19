package com.satecho.agrosafe.platform.apiservice.onboarding.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.valueobjects.CropType;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "farms")
@Getter
@Setter
@NoArgsConstructor
public class FarmPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 500)
    private String location;

    @Column(nullable = false)
    private double hectares;

    @Enumerated(EnumType.STRING)
    @Column(name = "crop_type", nullable = false, length = 30)
    private CropType cropType;
}