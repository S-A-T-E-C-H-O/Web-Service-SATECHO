package com.satecho.agrosafe.platform.apiservice.subscriptions.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.shared.infrastructure.security.SecurityContextUtil;
import com.satecho.agrosafe.platform.apiservice.shared.interfaces.rest.transform.ResponseEntityAssembler;
import com.satecho.agrosafe.platform.apiservice.subscriptions.application.commandservices.SubscriptionCommandService;
import com.satecho.agrosafe.platform.apiservice.subscriptions.application.queryservices.InvoiceQueryService;
import com.satecho.agrosafe.platform.apiservice.subscriptions.application.queryservices.PaymentQueryService;
import com.satecho.agrosafe.platform.apiservice.subscriptions.application.queryservices.SubscriptionQueryService;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.exceptions.InvoiceNotFoundException;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.exceptions.SubscriptionNotFoundException;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates.Invoice;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates.Payment;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates.Subscription;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.commands.CancelSubscriptionCommand;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.commands.CreateSubscriptionCommand;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.commands.UpdatePlanCommand;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.valueobjects.PlanType;
import com.satecho.agrosafe.platform.apiservice.subscriptions.interfaces.rest.resources.*;
import com.satecho.agrosafe.platform.apiservice.subscriptions.interfaces.rest.transform.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/subscriptions", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Subscriptions", description = "Subscription management endpoints")
@PreAuthorize("isAuthenticated()")
public class SubscriptionController {

    private final SubscriptionCommandService subscriptionCommandService;
    private final SubscriptionQueryService subscriptionQueryService;
    private final InvoiceQueryService invoiceQueryService;
    private final PaymentQueryService paymentQueryService;

    public SubscriptionController(SubscriptionCommandService subscriptionCommandService,
                                   SubscriptionQueryService subscriptionQueryService,
                                   InvoiceQueryService invoiceQueryService,
                                   PaymentQueryService paymentQueryService) {
        this.subscriptionCommandService = subscriptionCommandService;
        this.subscriptionQueryService = subscriptionQueryService;
        this.invoiceQueryService = invoiceQueryService;
        this.paymentQueryService = paymentQueryService;
    }

    @GetMapping("/me")
    public ResponseEntity<SubscriptionResource> getCurrentSubscription() {
        Long userId = SecurityContextUtil.getCurrentUserId();
        Subscription subscription = subscriptionQueryService.findByUserId(userId)
                .orElseThrow(() -> new SubscriptionNotFoundException("No subscription found for current user"));
        return ResponseEntity.ok(SubscriptionResourceFromEntityAssembler.toResourceFromEntity(subscription));
    }

    @PostMapping
    public ResponseEntity<?> createSubscription(@RequestBody CreateSubscriptionResource resource) {
        Long userId = SecurityContextUtil.getCurrentUserId();
        CreateSubscriptionCommand command = CreateSubscriptionCommandFromResourceAssembler.toCommandFromResource(resource, userId);
        var result = subscriptionCommandService.createSubscription(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, SubscriptionResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @PutMapping("/me")
    public ResponseEntity<?> updatePlan(@RequestBody UpdatePlanResource resource) {
        Long userId = SecurityContextUtil.getCurrentUserId();
        Subscription existingSubscription = subscriptionQueryService.findByUserId(userId)
                .orElseThrow(() -> new SubscriptionNotFoundException("No subscription found for current user"));
        UpdatePlanCommand command = UpdatePlanCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = subscriptionCommandService.updatePlan(existingSubscription.getId(), command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, SubscriptionResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }

    @PostMapping("/me/cancel")
    public ResponseEntity<?> cancelSubscription() {
        Long userId = SecurityContextUtil.getCurrentUserId();
        Subscription existingSubscription = subscriptionQueryService.findByUserId(userId)
                .orElseThrow(() -> new SubscriptionNotFoundException("No subscription found for current user"));
        CancelSubscriptionCommand command = new CancelSubscriptionCommand(existingSubscription.getId(), userId);
        var result = subscriptionCommandService.cancelSubscription(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, SubscriptionResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.OK);
    }

    @GetMapping("/me/invoices")
    public ResponseEntity<List<InvoiceResource>> getCurrentUserInvoices() {
        Long userId = SecurityContextUtil.getCurrentUserId();
        List<Invoice> invoices = invoiceQueryService.findByUserId(userId);
        return ResponseEntity.ok(invoices.stream().map(InvoiceResourceFromEntityAssembler::toResourceFromEntity).collect(Collectors.toList()));
    }

    @GetMapping("/me/invoices/{invoiceId}")
    public ResponseEntity<InvoiceResource> getInvoiceById(@PathVariable Long invoiceId) {
        Long userId = SecurityContextUtil.getCurrentUserId();
        Invoice invoice = invoiceQueryService.findById(invoiceId)
                .orElseThrow(() -> new InvoiceNotFoundException(invoiceId));
        if (!invoice.getUserId().equals(userId)) throw new InvoiceNotFoundException(invoiceId);
        return ResponseEntity.ok(InvoiceResourceFromEntityAssembler.toResourceFromEntity(invoice));
    }

    @GetMapping("/me/payments")
    public ResponseEntity<List<PaymentResource>> getCurrentUserPayments() {
        Long userId = SecurityContextUtil.getCurrentUserId();
        List<Payment> payments = paymentQueryService.findByUserId(userId);
        return ResponseEntity.ok(payments.stream().map(PaymentResourceFromEntityAssembler::toResourceFromEntity).collect(Collectors.toList()));
    }

    @GetMapping("/plans")
    public ResponseEntity<List<PlanResource>> getAvailablePlans() {
        return ResponseEntity.ok(Arrays.stream(PlanType.values())
                .map(PlanResourceFromPlanTypeAssembler::toResourceFromPlanType).collect(Collectors.toList()));
    }
}
