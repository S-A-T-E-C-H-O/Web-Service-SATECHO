package com.satecho.agrosafe.platform.apiservice.billing.application.internal.eventhandlers;

import com.satecho.agrosafe.platform.apiservice.billing.application.commandservices.PlanCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Event handler that ensures default subscription plans (FREE, BASIC, PRO) are seeded on startup.
 * Automatically runs once the ApplicationReadyEvent is fired.
 */
@Slf4j
@Service
public class PlanSeedEventHandler {

    /**
     * Command service used to execute the plan seeding operation.
     */
    private final PlanCommandService planCommandService;

    /**
     * Constructs a new PlanSeedEventHandler.
     *
     * @param planCommandService the command service for seeding plans
     */
    public PlanSeedEventHandler(PlanCommandService planCommandService) {
        this.planCommandService = planCommandService;
    }

    /**
     * Listens to the {@link ApplicationReadyEvent} and initiates the plan seeding.
     *
     * @param event the application ready event
     */
    @EventListener
    public void on(ApplicationReadyEvent event) {
        log.info("Verifying default subscription plans are seeded");
        planCommandService.seedDefaultPlans();
    }
}
