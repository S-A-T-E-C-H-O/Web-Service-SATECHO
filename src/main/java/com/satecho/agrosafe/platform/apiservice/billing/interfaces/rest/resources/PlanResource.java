package com.satecho.agrosafe.platform.apiservice.billing.interfaces.rest.resources;

import java.util.List;

/**
 * REST response resource representation of a Subscription Plan.
 * Field names align with UI mapping components of the Mobile App.
 *
 * @param id the plan identifier
 * @param tier the subscription tier key
 * @param name the subscription plan name
 * @param price the plan monthly cost
 * @param maxDevices the device allowance limit (null for unlimited)
 * @param maxFarms the farm management limit (null for unlimited)
 * @param features list of feature descriptions provided by this plan
 */
public record PlanResource(Long id, String tier, String name, Double price, Integer maxDevices,
                            Integer maxFarms, List<String> features) {
}
