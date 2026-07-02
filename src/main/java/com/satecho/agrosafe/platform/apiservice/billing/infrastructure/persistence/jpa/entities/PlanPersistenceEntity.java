package com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects.PlanTier;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "plans")
@Getter
@Setter
@NoArgsConstructor
public class PlanPersistenceEntity extends AuditableAbstractPersistenceEntity {
    @Enumerated(EnumType.STRING)
    @Column(name = "tier", nullable = false, unique = true, length = 20)
    private PlanTier tier;
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Column(name = "price_monthly")
    private Double priceMonthly;
    @Column(name = "max_devices")
    private Integer maxDevices;
    @Column(name = "max_farms")
    private Integer maxFarms;
    @Column(name = "max_hectares")
    private Integer maxHectares;
    @ElementCollection
    @CollectionTable(name = "plan_features", joinColumns = @JoinColumn(name = "plan_id"))
    @Column(name = "feature", length = 200)
    private List<String> features = new ArrayList<>();
}
