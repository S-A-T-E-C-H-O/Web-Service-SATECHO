package com.satecho.agrosafe.platform.apiservice.subscriptions.interfaces.rest;

import com.satecho.agrosafe.platform.apiservice.subscriptions.application.commandservices.PaymentCommandService;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.exceptions.PaymentFailedException;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates.Payment;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.commands.ProcessPaymentCommand;
import com.satecho.agrosafe.platform.apiservice.subscriptions.interfaces.rest.resources.PaymentResource;
import com.satecho.agrosafe.platform.apiservice.subscriptions.interfaces.rest.resources.PaymentWebhookResource;
import com.satecho.agrosafe.platform.apiservice.subscriptions.interfaces.rest.transform.PaymentResourceFromEntityAssembler;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/webhooks", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Webhooks", description = "Payment webhook endpoints")
public class PaymentWebhookController {

    private final PaymentCommandService paymentCommandService;

    public PaymentWebhookController(PaymentCommandService paymentCommandService) {
        this.paymentCommandService = paymentCommandService;
    }

    @PostMapping("/payments")
    public ResponseEntity<?> handlePaymentWebhook(@RequestBody PaymentWebhookResource resource) {
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
}
