package com.satecho.agrosafe.platform.billing.application;

import com.satecho.agrosafe.platform.apiservice.billing.application.internal.commandservices.SubscriptionCommandServiceImpl;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Invoice;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Plan;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Subscription;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.commands.SubscribeCommand;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects.PlanTier;
import com.satecho.agrosafe.platform.apiservice.billing.domain.repositories.InvoiceRepository;
import com.satecho.agrosafe.platform.apiservice.billing.domain.repositories.PlanRepository;
import com.satecho.agrosafe.platform.apiservice.billing.domain.repositories.SubscriptionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("SubscriptionCommandServiceImpl")
class SubscriptionCommandServiceImplTest {

    @Mock SubscriptionRepository subscriptionRepository;
    @Mock PlanRepository planRepository;
    @Mock InvoiceRepository invoiceRepository;
    @InjectMocks SubscriptionCommandServiceImpl service;

    @Test
    @DisplayName("subscribe: unknown plan tier returns failure")
    void subscribe_unknownTier_returnsFailure() {
        when(planRepository.findByTier(PlanTier.PRO)).thenReturn(Optional.empty());
        var result = service.subscribe(new SubscribeCommand(1L, PlanTier.PRO));
        assertThat(result.isFailure()).isTrue();
    }

    @Test
    @DisplayName("subscribe: new subscription is created and an invoice is issued")
    void subscribe_newUser_createsSubscriptionAndInvoice() {
        var plan = new Plan(PlanTier.BASIC, "Basic", 19.99, 10, 3, List.of());
        plan.setId(5L);
        when(planRepository.findByTier(PlanTier.BASIC)).thenReturn(Optional.of(plan));
        when(subscriptionRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(subscriptionRepository.save(any(Subscription.class))).thenAnswer(inv -> {
            Subscription s = inv.getArgument(0);
            s.setId(100L);
            return s;
        });
        when(invoiceRepository.save(any(Invoice.class))).thenAnswer(inv -> inv.getArgument(0));

        var result = service.subscribe(new SubscribeCommand(1L, PlanTier.BASIC));

        assertThat(result.isSuccess()).isTrue();
        assertThat(result.toOptional().get().getPlanId()).isEqualTo(5L);
        verify(invoiceRepository).save(any(Invoice.class));
    }

    @Test
    @DisplayName("cancel: unknown subscription returns failure")
    void cancel_noSubscription_returnsFailure() {
        when(subscriptionRepository.findByUserId(1L)).thenReturn(Optional.empty());
        var result = service.cancel(1L);
        assertThat(result.isFailure()).isTrue();
    }

    @Test
    @DisplayName("cancel: existing subscription is marked CANCELLED")
    void cancel_existingSubscription_marksCancelled() {
        var subscription = new Subscription(1L, 5L);
        when(subscriptionRepository.findByUserId(1L)).thenReturn(Optional.of(subscription));
        when(subscriptionRepository.save(subscription)).thenReturn(subscription);

        var result = service.cancel(1L);

        assertThat(result.isSuccess()).isTrue();
        assertThat(subscription.getStatus().name()).isEqualTo("CANCELLED");
    }
}
