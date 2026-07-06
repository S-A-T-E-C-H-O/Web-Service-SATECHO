package com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects.PlanTier;
import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a subscription plan tier with device and farm limits (EP-012-TS001).
 */
@Getter
public class Plan extends AuditableAbstractAggregateRoot<Plan> {
    /**
     * Unique identifier of the plan.
     */
    @Setter private Long id;

    /**
     * The tier classification (e.g., FREE, BASIC, PRO).
     */
    @Setter private PlanTier tier;

    /**
     * The user-friendly name of the plan tier.
     */
    @Setter private String name;

    /**
     * The monthly cost of the plan tier.
     */
    @Setter private Double priceMonthly;

    /**
     * The maximum number of devices a user is allowed to register on this plan.
     * A value of null indicates unlimited devices.
     */
    @Setter private Integer maxDevices;

    /**
     * The maximum number of farms a user is allowed to manage on this plan.
     * A value of null indicates unlimited farms.
     */
    @Setter private Integer maxFarms;

    /**
     * A list of feature descriptions supported by this plan.
     */
    @Setter private List<String> features;

    /**
     * Constructs a default Plan with an empty features list.
     */
    public Plan() {
        this.features = new ArrayList<>();
    }

    /**
     * Constructs a Plan with full attributes.
     *
     * @param tier the plan tier classification
     * @param name the plan name
     * @param priceMonthly the monthly cost
     * @param maxDevices the device limit (null for unlimited)
     * @param maxFarms the farm limit (null for unlimited)
     * @param features list of feature description strings
     */
    public Plan(PlanTier tier, String name, Double priceMonthly, Integer maxDevices, Integer maxFarms, List<String> features) {
        this.tier = tier;
        this.name = name;
        this.priceMonthly = priceMonthly;
        this.maxDevices = maxDevices;
        this.maxFarms = maxFarms;
        this.features = features != null ? features : new ArrayList<>();
    }

    /**
     * Determines whether the plan allows registering more devices than the current count.
     *
     * @param currentDeviceCount the number of devices currently registered
     * @return true if the plan permits registering another device; false otherwise
     */
    public boolean allowsMoreDevices(int currentDeviceCount) {
        return maxDevices == null || currentDeviceCount < maxDevices;
    }
}
