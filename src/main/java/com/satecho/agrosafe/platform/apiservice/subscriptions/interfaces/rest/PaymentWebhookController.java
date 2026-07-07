package com.satecho.agrosafe.platform.apiservice.subscriptions.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.subscriptions.application.commandservices.PaymentCommandService;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.exceptions.PaymentFailedException;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates.Payment;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.commands.ProcessPaymentCommand;
import com.satecho.agrosafe.platform.apiservice.subscriptions.interfaces.rest.resources.PaymentWebhookResource;
import com.satecho.agrosafe.platform.apiservice.subscriptions.interfaces.rest.transform.PaymentResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;

/**
 * Payment provider webhook. The route is permitted without a JWT (an external
 * provider cannot obtain one) but every request must carry the shared-secret
 * signature header; without a configured secret the endpoint rejects
 * everything (closed by default).
 */
@RestController
@RequestMapping(value = "/api/v1/webhooks", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Webhooks", description = "Payment webhook endpoints")
public class PaymentWebhookController {

    private static final Logger log = LoggerFactory.getLogger(PaymentWebhookController.class);
    private static final String SIGNATURE_HEADER = "X-Webhook-Signature";

    private final PaymentCommandService paymentCommandService;
    private final String webhookSecret;

    public PaymentWebhookController(PaymentCommandService paymentCommandService,
                                    @Value("${app.webhooks.payment-secret:}") String webhookSecret) {
        this.paymentCommandService = paymentCommandService;
        this.webhookSecret = webhookSecret;
    }

    @PostMapping("/payments")
    public ResponseEntity<?> handlePaymentWebhook(
            @RequestHeader(value = SIGNATURE_HEADER, required = false) String signature,
            @RequestBody PaymentWebhookResource resource) {
        if (!isValidSignature(signature)) {
            log.warn("Rejected payment webhook — missing or invalid {} header", SIGNATURE_HEADER);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "INVALID_WEBHOOK_SIGNATURE"));
        }

        if (resource.userId() == null || resource.subscriptionId() == null) {
            throw new PaymentFailedException("Missing required fields in webhook payload");
        }

        ProcessPaymentCommand command = new ProcessPaymentCommand(
                resource.userId(), resource.subscriptionId(), resource.invoiceId(),
                resource.amount(), resource.currency() != null ? resource.currency() : "PEN",
                resource.paymentMethod(), resource.externalTransactionId());

        var result = paymentCommandService.processPayment(command);
        if (result.isFailure()) {
            return ResponseEntity.badRequest().body(result.toOptional().isPresent() ? null : result);
        }
        Payment payment = result.toOptional().orElseThrow();
        return ResponseEntity.status(HttpStatus.CREATED).body(PaymentResourceFromEntityAssembler.toResourceFromEntity(payment));
    }

    private boolean isValidSignature(String signature) {
        if (webhookSecret == null || webhookSecret.isBlank()) {
            // No secret configured → nothing can be verified → reject all.
            return false;
        }
        if (signature == null || signature.isBlank()) {
            return false;
        }
        return MessageDigest.isEqual(
                webhookSecret.getBytes(StandardCharsets.UTF_8),
                signature.getBytes(StandardCharsets.UTF_8));
    }
}
