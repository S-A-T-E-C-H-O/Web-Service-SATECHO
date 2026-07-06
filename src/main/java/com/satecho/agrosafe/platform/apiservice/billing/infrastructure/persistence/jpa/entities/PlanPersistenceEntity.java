package com.satecho.agrosafe.platform.apiservice.billing.infrastructure.persistence.jpa.entities;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects.PlanTier;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * JPA entity mapping representing subscription plan tiers in the 'plans' table.
 */
@Entity
@Table(name = "plans")
@Getter
@Setter
@NoArgsConstructor
public class PlanPersistenceEntity extends AuditableAbstractPersistenceEntity {

    /**
     * The plan tier key enum.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tier", nullable = false, unique = true, length = 20)
    private PlanTier tier;

    /**
     * The human-friendly display name of the plan tier.
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * The monthly pricing charge.
     */
    @Column(name = "price_monthly")
    private Double priceMonthly;

    /**
     * The maximum allowable IoT devices.
     */
    @Column(name = "max_devices")
    private Integer maxDevices;

    /**
     * The maximum allowable farms.
     */
    @Column(name = "max_farms")
    private Integer maxFarms;

    /**
     * Sub-table mappings of promotional or functional features.
     */
    @ElementCollection
    @CollectionTable(name = "plan_features", joinColumns = @JoinColumn(name = "plan_id"))
    @Column(name = "feature", length = 200)
    private List<String> features = new ArrayList<>();
}
