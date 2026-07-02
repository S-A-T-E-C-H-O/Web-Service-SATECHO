package com.satecho.agrosafe.platform.apiservice.billing.application.internal.eventhandlers;

import com.satecho.agrosafe.platform.apiservice.billing.application.commandservices.PlanCommandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/** Ensures the default FREE/BASIC/PRO plans exist on startup, mirroring role seeding. */
@Slf4j
@Service
public class PlanSeedEventHandler {

    private final PlanCommandService planCommandService;

    public PlanSeedEventHandler(PlanCommandService planCommandService) {
        this.planCommandService = planCommandService;
    }

    @EventListener
    public void on(ApplicationReadyEvent event) {
        log.info("Verifying default subscription plans are seeded");
        planCommandService.seedDefaultPlans();
    }
}
