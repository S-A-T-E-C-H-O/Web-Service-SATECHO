package com.satecho.agrosafe.platform.apiservice.subscriptions.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import com.satecho.agrosafe.platform.apiservice.subscriptions.application.commandservices.PaymentCommandService;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.exceptions.PaymentFailedException;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.exceptions.SubscriptionNotFoundException;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates.Payment;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.aggregates.Subscription;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.commands.ProcessPaymentCommand;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.events.PaymentProcessedEvent;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.repositories.InvoiceRepository;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.repositories.PaymentRepository;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.repositories.SubscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class PaymentCommandServiceImpl implements PaymentCommandService {

    private final PaymentRepository paymentRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final InvoiceRepository invoiceRepository;

    public PaymentCommandServiceImpl(PaymentRepository paymentRepository,
                                      SubscriptionRepository subscriptionRepository,
                                      InvoiceRepository invoiceRepository) {
        this.paymentRepository = paymentRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.invoiceRepository = invoiceRepository;
    }

    @Override
    public Result<Payment, ApplicationError> processPayment(ProcessPaymentCommand command) {
        Optional<Payment> existingPayment = paymentRepository.findByTransactionId(command.externalTransactionId());
        if (existingPayment.isPresent()) {
            return Result.failure(ApplicationError.conflict("Payment", "Duplicate transaction: " + command.externalTransactionId()));
        }

        Subscription subscription = subscriptionRepository.findById(command.subscriptionId())
                .orElseThrow(() -> new SubscriptionNotFoundException(command.subscriptionId()));

        if (!subscription.getUserId().equals(command.userId())) {
            return Result.failure(ApplicationError.notFound("Subscription", String.valueOf(command.subscriptionId())));
        }

        Payment payment = new Payment(
                command.userId(), command.subscriptionId(), command.invoiceId(),
                command.amount(), command.currency(), command.paymentMethod(),
                command.externalTransactionId());

        Payment savedPayment = paymentRepository.save(payment);

        if (command.invoiceId() != null) {
            invoiceRepository.findById(command.invoiceId()).ifPresent(invoice -> {
                invoice.markAsPaid(command.paymentMethod().name(), command.externalTransactionId());
                invoiceRepository.save(invoice);
            });
        }

        savedPayment.addDomainEvent(new PaymentProcessedEvent(
                savedPayment.getId(), command.userId(), command.subscriptionId(),
                command.invoiceId(), command.amount(), command.currency(),
                command.paymentMethod().name(), command.externalTransactionId(),
                savedPayment.getProcessedAt()));

        return Result.success(savedPayment);
    }
}
