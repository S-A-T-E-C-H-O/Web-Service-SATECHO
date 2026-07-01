package com.satecho.agrosafe.platform.apiservice.subscriptions.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import com.satecho.agrosafe.platform.apiservice.subscriptions.application.commandservices.SubscriptionCommandService;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.exceptions.SubscriptionNotFoundException;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates.Invoice;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates.Subscription;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.commands.CancelSubscriptionCommand;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.commands.CreateSubscriptionCommand;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.commands.UpdatePlanCommand;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.events.SubscriptionCanceledEvent;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.events.SubscriptionCreatedEvent;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.valueobjects.PlanType;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.repositories.InvoiceRepository;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.repositories.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
@Transactional
public class SubscriptionCommandServiceImpl implements SubscriptionCommandService {

    private final SubscriptionRepository subscriptionRepository;
    private final InvoiceRepository invoiceRepository;

    public SubscriptionCommandServiceImpl(SubscriptionRepository subscriptionRepository, InvoiceRepository invoiceRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public Result<Subscription, ApplicationError> createSubscription(CreateSubscriptionCommand command) {
        Optional<Subscription> existingSubscription = subscriptionRepository.findByUserId(command.userId());
        if (existingSubscription.isPresent()) {
            Subscription subscription = existingSubscription.get();
            subscription.upgradePlan(command.planType());
            subscription.updateBillingCycle(command.billingCycle());
            subscription.activate();
            Subscription savedSubscription = subscriptionRepository.save(subscription);
            generateInvoice(savedSubscription);
            savedSubscription.addDomainEvent(new SubscriptionCreatedEvent(
                    savedSubscription.getId(), command.userId(),
                    command.planType().name(), command.billingCycle().name()));
            return Result.success(savedSubscription);
        }

        Subscription subscription = new Subscription(command.userId(), command.planType(), command.billingCycle());
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        savedSubscription.addDomainEvent(new SubscriptionCreatedEvent(
                savedSubscription.getId(), command.userId(),
                command.planType().name(), command.billingCycle().name()));
        return Result.success(savedSubscription);
    }

    @Override
    public Result<Subscription, ApplicationError> createTrialSubscription(Long userId) {
        Optional<Subscription> existingSubscription = subscriptionRepository.findByUserId(userId);
        if (existingSubscription.isPresent()) {
            return Result.success(existingSubscription.get());
        }

        Subscription subscription = Subscription.createTrial(userId);
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        savedSubscription.addDomainEvent(new SubscriptionCreatedEvent(
                savedSubscription.getId(), userId, PlanType.STARTER.name(), "MONTHLY"));
        return Result.success(savedSubscription);
    }

    @Override
    public Result<Subscription, ApplicationError> updatePlan(Long subscriptionId, UpdatePlanCommand command) {
        Subscription subscription = subscriptionRepository.findById(subscriptionId)
                .orElseThrow(() -> new SubscriptionNotFoundException(subscriptionId));

        if (command.planType() != null) subscription.upgradePlan(command.planType());
        if (command.billingCycle() != null) subscription.updateBillingCycle(command.billingCycle());

        Subscription savedSubscription = subscriptionRepository.save(subscription);
        if (command.planType() != null) generateInvoice(savedSubscription);
        return Result.success(savedSubscription);
    }

    @Override
    public Result<Subscription, ApplicationError> cancelSubscription(CancelSubscriptionCommand command) {
        Subscription subscription = subscriptionRepository.findById(command.subscriptionId())
                .orElseThrow(() -> new SubscriptionNotFoundException(command.subscriptionId()));

        if (!subscription.getUserId().equals(command.userId())) {
            return Result.failure(ApplicationError.notFound("Subscription", String.valueOf(command.subscriptionId())));
        }

        subscription.cancel();
        Subscription savedSubscription = subscriptionRepository.save(subscription);
        savedSubscription.addDomainEvent(new SubscriptionCanceledEvent(
                savedSubscription.getId(), command.userId(), savedSubscription.getCanceledAt()));
        return Result.success(savedSubscription);
    }

    private void generateInvoice(Subscription subscription) {
        PlanType planType = subscription.getPlanType();
        double amount = planType.getPricePerHaPerMonth();

        Instant billingPeriodStart = Instant.now();
        Instant billingPeriodEnd;

        switch (subscription.getBillingCycle()) {
            case MONTHLY -> billingPeriodEnd = billingPeriodStart.plus(30, ChronoUnit.DAYS);
            case QUARTERLY -> { billingPeriodEnd = billingPeriodStart.plus(90, ChronoUnit.DAYS); amount = amount * 3; }
            case ANNUAL -> { billingPeriodEnd = billingPeriodStart.plus(365, ChronoUnit.DAYS); amount = amount * 12; }
            default -> billingPeriodEnd = billingPeriodStart.plus(30, ChronoUnit.DAYS);
        }

        Instant dueDate = billingPeriodEnd.plus(15, ChronoUnit.DAYS);
        String description = "Invoice for " + planType.name() + " plan - " + subscription.getBillingCycle().name();

        Invoice invoice = new Invoice(
                subscription.getUserId(), subscription.getId(), amount,
                description, billingPeriodStart, billingPeriodEnd, dueDate);
        invoiceRepository.save(invoice);
    }
}
