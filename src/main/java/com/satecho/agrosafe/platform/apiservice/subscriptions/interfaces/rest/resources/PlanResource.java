package com.satecho.agrosafe.platform.apiservice.subscriptions.interfaces.rest.resources;

import java.util.List;

public record PlanResource(String name, double pricePerHaPerMonth, int maxFarms, int maxDevices, List<String> features) {}
