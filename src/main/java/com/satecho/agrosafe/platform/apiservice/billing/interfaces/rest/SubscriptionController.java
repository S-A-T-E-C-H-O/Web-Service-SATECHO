package com.satecho.agrosafe.platform.apiservice.billing.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.billing.application.commandservices.SubscriptionCommandService;
import com.satecho.agrosafe.platform.apiservice.billing.application.queryservices.InvoiceQueryService;
import com.satecho.agrosafe.platform.apiservice.billing.application.queryservices.PlanQueryService;
import com.satecho.agrosafe.platform.apiservice.billing.application.queryservices.SubscriptionQueryService;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Invoice;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Plan;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Subscription;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.commands.SubscribeCommand;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects.PlanTier;
import com.satecho.agrosafe.platform.apiservice.billing.interfaces.rest.resources.InvoiceResource;
import com.satecho.agrosafe.platform.apiservice.billing.interfaces.rest.resources.PlanResource;
import com.satecho.agrosafe.platform.apiservice.billing.interfaces.rest.resources.SubscribeResource;
import com.satecho.agrosafe.platform.apiservice.billing.interfaces.rest.resources.SubscriptionResource;
import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.security.SecurityContextUtil;
import com.satecho.agrosafe.platform.apiservice.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller class providing endpoints for managing plans, user subscriptions, and billing invoices.
 */
@RestController
@RequestMapping(value = "/api/v1/billing/subscriptions", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Subscriptions", description = "Plans, subscriptions, and billing history (EP-012)")
@PreAuthorize("isAuthenticated()")
public class SubscriptionController {

    /**
     * Query service for plans.
     */
    private final PlanQueryService planQueryService;

    /**
     * Query service for subscriptions.
     */
    private final SubscriptionQueryService subscriptionQueryService;

    /**
     * Command service for subscription modifications.
     */
    private final SubscriptionCommandService subscriptionCommandService;

    /**
     * Query service for retrieving invoices.
     */
    private final InvoiceQueryService invoiceQueryService;

    /**
     * Constructs a new SubscriptionController.
     *
     * @param planQueryService the plan query service
     * @param subscriptionQueryService the subscription query service
     * @param subscriptionCommandService the subscription command service
     * @param invoiceQueryService the invoice query service
     */
    public SubscriptionController(PlanQueryService planQueryService, SubscriptionQueryService subscriptionQueryService,
                                   SubscriptionCommandService subscriptionCommandService, InvoiceQueryService invoiceQueryService) {
        this.planQueryService = planQueryService;
        this.subscriptionQueryService = subscriptionQueryService;
        this.subscriptionCommandService = subscriptionCommandService;
        this.invoiceQueryService = invoiceQueryService;
    }

    /**
     * Retrieves all active plan options.
     *
     * @return a list of plan resource records
     */
    @GetMapping("/plans")
    public ResponseEntity<List<PlanResource>> getPlans() {
        var plans = planQueryService.findAll().stream().map(this::toResource).toList();
        return ResponseEntity.ok(plans);
    }

    /**
     * Retrieves the current authenticated user's subscription record.
     *
     * @return a subscription resource record, or noContent if not found
     */
    @GetMapping("/me")
    public ResponseEntity<SubscriptionResource> getCurrentSubscription() {
        Long userId = SecurityContextUtil.getCurrentUserId();
        var subscription = subscriptionQueryService.findByUserId(userId);
        if (subscription.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(toResource(subscription.get()));
    }

    /**
     * Creates or updates the subscription of the current authenticated user to a target tier.
     *
     * @param resource the subscribe body holding target tier name
     * @return response representing the transaction outcome
     */
    @PostMapping("/me")
    public ResponseEntity<?> subscribe(@RequestBody SubscribeResource resource) {
        Long userId = SecurityContextUtil.getCurrentUserId();
        PlanTier tier;
        try {
            tier = PlanTier.valueOf(resource.planTier().toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.badRequest().build();
        }
        var result = subscriptionCommandService.subscribe(new SubscribeCommand(userId, tier));
        return ResponseEntityAssembler.toResponseEntityFromResult(result, this::toResource, HttpStatus.OK);
    }

    /**
     * Cancels the active subscription of the current user.
     *
     * @return response representing the cancellation outcome
     */
    @PostMapping("/me/cancel")
    public ResponseEntity<?> cancel() {
        Long userId = SecurityContextUtil.getCurrentUserId();
        var result = subscriptionCommandService.cancel(userId);
        return ResponseEntityAssembler.toResponseEntityFromResult(result, this::toResource, HttpStatus.OK);
    }

    /**
     * Retrieves billing history invoices generated for the current user.
     *
     * @return list of invoice resource records
     */
    @GetMapping("/me/invoices")
    public ResponseEntity<List<InvoiceResource>> getInvoices() {
        Long userId = SecurityContextUtil.getCurrentUserId();
        return ResponseEntity.ok(invoiceQueryService.findByUserId(userId).stream().map(this::toResource).toList());
    }

    /**
     * Retrieves payments billed to the current user (aliases to invoices).
     *
     * @return list of invoice payment records
     */
    @GetMapping("/me/payments")
    public ResponseEntity<List<InvoiceResource>> getPayments() {
        Long userId = SecurityContextUtil.getCurrentUserId();
        return ResponseEntity.ok(invoiceQueryService.findByUserId(userId).stream().map(this::toResource).toList());
    }

    /**
     * Helper mapping method to convert a {@link Plan} entity to a {@link PlanResource}.
     *
     * @param p the plan entity
     * @return the plan resource mapping
     */
    private PlanResource toResource(Plan p) {
        return new PlanResource(p.getId(), p.getTier().name(), p.getName(), p.getPriceMonthly(),
                p.getMaxDevices(), p.getMaxFarms(), p.getFeatures());
    }

    /**
     * Helper mapping method to convert a {@link Subscription} entity to a {@link SubscriptionResource}.
     *
     * @param s the subscription entity
     * @return the subscription resource mapping
     */
    private SubscriptionResource toResource(Subscription s) {
        var tierName = planQueryService.findById(s.getPlanId()).map(p -> p.getTier().name()).orElse(null);
        return new SubscriptionResource(s.getId(), tierName, s.getStatus().name(), s.getBillingCycle());
    }

    /**
     * Helper mapping method to convert an {@link Invoice} entity to an {@link InvoiceResource}.
     *
     * @param i the invoice entity
     * @return the invoice resource mapping
     */
    private InvoiceResource toResource(Invoice i) {
        return new InvoiceResource(i.getId(), i.getAmount(), i.getCurrency(), i.getStatus().name(),
                i.getDescription(), i.getIssuedAt());
    }
}
