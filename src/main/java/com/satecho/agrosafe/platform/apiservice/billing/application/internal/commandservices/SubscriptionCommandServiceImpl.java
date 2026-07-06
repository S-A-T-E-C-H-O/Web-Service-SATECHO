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

/**
 * Implementation of {@link SubscriptionCommandService}.
 * Handles business operations for subscribing and cancelling subscriptions, ensuring constraints are met.
 */
@Service
@Transactional
public class SubscriptionCommandServiceImpl implements SubscriptionCommandService {

    /**
     * Repository for managing subscription persistence.
     */
    private final SubscriptionRepository subscriptionRepository;

    /**
     * Repository for querying and persisting plan definitions.
     */
    private final PlanRepository planRepository;

    /**
     * Repository for persisting generated invoices.
     */
    private final InvoiceRepository invoiceRepository;

    /**
     * Repository for querying user's registered IoT devices to enforce plan limits.
     */
    private final DeviceRepository deviceRepository;

    /**
     * Constructs a new SubscriptionCommandServiceImpl.
     *
     * @param subscriptionRepository the repository for managing subscriptions
     * @param planRepository the repository for managing plans
     * @param invoiceRepository the repository for managing invoices
     * @param deviceRepository the repository for managing IoT devices
     */
    public SubscriptionCommandServiceImpl(SubscriptionRepository subscriptionRepository, PlanRepository planRepository,
                                           InvoiceRepository invoiceRepository, DeviceRepository deviceRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.planRepository = planRepository;
        this.invoiceRepository = invoiceRepository;
        this.deviceRepository = deviceRepository;
    }

    /**
     * Processes a subscription request for a user.
     * Enforces the constraint that downgrades cannot exceed the device limits of the new target plan (EP-006-US014).
     * Creates or updates the user's subscription and records a PAID invoice representing the billing event.
     *
     * @param command the subscription command containing user ID and target plan tier
     * @return a {@link Result} containing the updated {@link Subscription} or an {@link ApplicationError}
     */
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

    /**
     * Cancels a user's subscription, marking it as cancelled in the repository.
     *
     * @param userId the ID of the user whose subscription is being cancelled
     * @return a {@link Result} containing the cancelled {@link Subscription} or an {@link ApplicationError}
     */
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
