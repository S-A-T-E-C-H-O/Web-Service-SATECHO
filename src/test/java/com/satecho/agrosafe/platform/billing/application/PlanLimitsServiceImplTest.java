package com.satecho.agrosafe.platform.billing.application;

import com.satecho.agrosafe.platform.apiservice.billing.application.internal.queryservices.PlanLimitsServiceImpl;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Plan;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.aggregates.Subscription;
import com.satecho.agrosafe.platform.apiservice.billing.domain.model.valueobjects.PlanTier;
import com.satecho.agrosafe.platform.apiservice.billing.domain.repositories.PlanRepository;
import com.satecho.agrosafe.platform.apiservice.billing.domain.repositories.SubscriptionRepository;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.aggregates.Device;
import com.satecho.agrosafe.platform.apiservice.iot.domain.model.valueobjects.DeviceType;
import com.satecho.agrosafe.platform.apiservice.iot.domain.repositories.DeviceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("PlanLimitsServiceImpl")
class PlanLimitsServiceImplTest {

    @Mock SubscriptionRepository subscriptionRepository;
    @Mock PlanRepository planRepository;
    @Mock DeviceRepository deviceRepository;
    @InjectMocks PlanLimitsServiceImpl service;

    @Test
    @DisplayName("canRegisterDevice: under the plan's device limit returns true")
    void canRegisterDevice_underLimit_returnsTrue() {
        var subscription = new Subscription(1L, 5L);
        subscription.setId(1L);
        var plan = new Plan(PlanTier.BASIC, "Basic", 19.99, 10, 3, List.of());
        plan.setId(5L);

        when(subscriptionRepository.findByUserId(1L)).thenReturn(Optional.of(subscription));
        when(planRepository.findById(5L)).thenReturn(Optional.of(plan));
        when(deviceRepository.findByUserId(1L)).thenReturn(List.of(new Device(1L, "SN-1", DeviceType.SOIL_SENSOR)));

        assertThat(service.canRegisterDevice(1L)).isTrue();
    }

    @Test
    @DisplayName("canRegisterDevice: at the plan's device limit returns false")
    void canRegisterDevice_atLimit_returnsFalse() {
        var subscription = new Subscription(1L, 5L);
        subscription.setId(1L);
        var plan = new Plan(PlanTier.FREE, "Free", 0.0, 2, 1, List.of());
        plan.setId(5L);

        when(subscriptionRepository.findByUserId(1L)).thenReturn(Optional.of(subscription));
        when(planRepository.findById(5L)).thenReturn(Optional.of(plan));
        when(deviceRepository.findByUserId(1L)).thenReturn(List.of(
                new Device(1L, "SN-1", DeviceType.SOIL_SENSOR), new Device(1L, "SN-2", DeviceType.SOIL_SENSOR)));

        assertThat(service.canRegisterDevice(1L)).isFalse();
    }

    @Test
    @DisplayName("canRegisterDevice: no subscription falls back to the FREE plan limit")
    void canRegisterDevice_noSubscription_fallsBackToFreePlan() {
        var freePlan = new Plan(PlanTier.FREE, "Free", 0.0, 2, 1, List.of());
        freePlan.setId(1L);

        when(subscriptionRepository.findByUserId(1L)).thenReturn(Optional.empty());
        when(planRepository.findByTier(PlanTier.FREE)).thenReturn(Optional.of(freePlan));
        when(deviceRepository.findByUserId(1L)).thenReturn(List.of(new Device(1L, "SN-1", DeviceType.SOIL_SENSOR),
                new Device(1L, "SN-2", DeviceType.SOIL_SENSOR)));

        assertThat(service.canRegisterDevice(1L)).isFalse();
    }

    @Test
    @DisplayName("canRegisterDevice: PRO plan with unlimited devices always returns true")
    void canRegisterDevice_unlimitedPlan_returnsTrue() {
        var subscription = new Subscription(1L, 5L);
        var plan = new Plan(PlanTier.PRO, "Pro", 49.99, null, null, List.of());
        plan.setId(5L);

        when(subscriptionRepository.findByUserId(1L)).thenReturn(Optional.of(subscription));
        when(planRepository.findById(5L)).thenReturn(Optional.of(plan));
        when(deviceRepository.findByUserId(1L)).thenReturn(List.of());

        assertThat(service.canRegisterDevice(1L)).isTrue();
    }
}
