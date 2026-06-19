package com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.events;

public class OnboardingCompletedEvent extends org.springframework.context.ApplicationEvent {
    private final Long userId;
    private final Long onboardingProgressId;
    private final int totalFarms;
    private final int totalZones;

    public OnboardingCompletedEvent(Object source, Long userId, Long onboardingProgressId,
                                    int totalFarms, int totalZones) {
        super(source);
        this.userId = userId;
        this.onboardingProgressId = onboardingProgressId;
        this.totalFarms = totalFarms;
        this.totalZones = totalZones;
    }

    public Long getUserId() { return userId; }
    public Long getOnboardingProgressId() { return onboardingProgressId; }
    public int getTotalFarms() { return totalFarms; }
    public int getTotalZones() { return totalZones; }
}