package com.satecho.agrosafe.platform.apiservice.billing.interfaces.rest.resources;

import java.util.List;

/** Field names match Mobile App's subscription_page.dart plan map reads. */
public record PlanResource(Long id, String tier, String name, Double price, Integer maxHectares,
                            Integer maxDevices, Integer maxFarms, List<String> features) {
}
