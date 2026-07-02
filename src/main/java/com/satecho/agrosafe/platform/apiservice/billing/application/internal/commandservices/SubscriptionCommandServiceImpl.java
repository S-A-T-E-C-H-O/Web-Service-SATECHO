package com.satecho.agrosafe.platform.apiservice.billing.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.billing.application.commandservices.SubscriptionCommandService;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Invoice;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Subscription;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.commands.SubscribeCommand;
import com.satecho.agrosafe.platform.apiservice.billing.domain.repositories.InvoiceRepository;
import com.satecho.agrosafe.platform.apiservice.billing.domain.repositories.PlanRepository;
import com.satecho.agrosafe.platform.apiservice.billing.domain.repositories.SubscriptionRepository;
import com.satecho.agrosafe.platform.apiservice.iot.domain.repositories.DeviceRepository;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SubscriptionCommandServiceImpl implements SubscriptionCommandService {

    private final SubscriptionRepository subscriptionRepository;
    private final PlanRepository planRepository;
    private final InvoiceRepository invoiceRepository;
    private final DeviceRepository deviceRepository;

    public SubscriptionCommandServiceImpl(SubscriptionRepository subscriptionRepository, PlanRepository planRepository,
                                           InvoiceRepository invoiceRepository, DeviceRepository deviceRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.planRepository = planRepository;
        this.invoiceRepository = invoiceRepository;
        this.deviceRepository = deviceRepository;
    }

    @Override
    public Result<Subscription, ApplicationError> subscribe(SubscribeCommand command) {
        var plan = planRepository.findByTier(command.planTier());
        if (plan.isEmpty()) {
            return Result.failure(ApplicationError.notFound("Plan", command.planTier().name()));
        }

        // EP-006-US014 Scenario 3: block a downgrade that would leave the farmer
        // with more active devices than the destination plan allows.
        if (plan.get().getMaxDevices() != null) {
            int currentDeviceCount = deviceRepository.findByUserId(command.userId()).size();
            if (currentDeviceCount > plan.get().getMaxDevices()) {
                return Result.failure(ApplicationError.conflict("Subscription",
                        "You have " + currentDeviceCount + " active devices, which exceeds the " +
                                plan.get().getName() + " plan's limit of " + plan.get().getMaxDevices() +
                                ". Deactivate devices before downgrading."));
            }
        }

        var subscription = subscriptionRepository.findByUserId(command.userId())
                .orElseGet(() -> new Subscription(command.userId(), plan.get().getId()));
        subscription.changePlan(plan.get().getId());
        var saved = subscriptionRepository.save(subscription);

        invoiceRepository.save(new Invoice(saved.getId(), command.userId(), plan.get().getPriceMonthly(),
                "Subscription to " + plan.get().getName() + " plan"));

        return Result.success(saved);
    }

    @Override
    public Result<Subscription, ApplicationError> cancel(Long userId) {
        var subscription = subscriptionRepository.findByUserId(userId);
        if (subscription.isEmpty()) {
            return Result.failure(ApplicationError.notFound("Subscription", "for user " + userId));
        }
        var s = subscription.get();
        s.cancel();
        return Result.success(subscriptionRepository.save(s));
    }
}
