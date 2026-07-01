package com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.valueobjects;

import java.util.List;

public enum PlanType {

    STARTER(15.0, 1, 5, List.of("Basic soil monitoring", "5 device limit", "1 farm", "Email support", "Standard analytics")),
    PRO(30.0, 5, 20, List.of("Advanced soil monitoring", "20 device limit", "5 farms", "Priority email support", "Advanced analytics", "Weather integration", "Irrigation recommendations")),
    ENTERPRISE(50.0, 20, 100, List.of("Full soil monitoring suite", "100 device limit", "20 farms", "24/7 phone & email support", "Enterprise analytics", "Weather integration", "AI-powered irrigation recommendations", "Dedicated agronomist", "API access", "Custom integrations"));

    private final double pricePerHaPerMonth;
    private final int maxFarms;
    private final int maxDevices;
    private final List<String> features;

    PlanType(double pricePerHaPerMonth, int maxFarms, int maxDevices, List<String> features) {
        this.pricePerHaPerMonth = pricePerHaPerMonth;
        this.maxFarms = maxFarms;
        this.maxDevices = maxDevices;
        this.features = features;
    }

    public double getPricePerHaPerMonth() { return pricePerHaPerMonth; }
    public int getMaxFarms() { return maxFarms; }
    public int getMaxDevices() { return maxDevices; }
    public List<String> getFeatures() { return features; }
}
