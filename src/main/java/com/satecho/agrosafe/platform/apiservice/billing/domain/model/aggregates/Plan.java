package com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects.PlanTier;
import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/** A subscription tier with device/farm limits (EP-012-TS001). */
@Getter
public class Plan extends AuditableAbstractAggregateRoot<Plan> {
    @Setter private Long id;
    @Setter private PlanTier tier;
    @Setter private String name;
    @Setter private Double priceMonthly;
    @Setter private Integer maxDevices;
    @Setter private Integer maxFarms;
    @Setter private Integer maxHectares;
    @Setter private List<String> features;

    public Plan() {
        this.features = new ArrayList<>();
    }

    public Plan(PlanTier tier, String name, Double priceMonthly, Integer maxDevices, Integer maxFarms, List<String> features) {
        this.tier = tier;
        this.name = name;
        this.priceMonthly = priceMonthly;
        this.maxDevices = maxDevices;
        this.maxFarms = maxFarms;
        this.maxHectares = null;
        this.features = features != null ? features : new ArrayList<>();
    }

    /** null maxDevices means unlimited. */
    public boolean allowsMoreDevices(int currentDeviceCount) {
        return maxDevices == null || currentDeviceCount < maxDevices;
    }

    /** null maxHectares means unlimited. */
    public boolean allowsMoreHectares(int currentHectareUsage) {
        return maxHectares == null || currentHectareUsage < maxHectares;
    }
}
