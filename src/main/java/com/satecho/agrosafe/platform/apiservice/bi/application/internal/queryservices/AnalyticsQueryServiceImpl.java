package com.satecho.agrosafe.platform.apiservice.bi.application.internal.queryservices;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.satecho.agrosafe.platform.apiservice.bi.application.queryservices.AnalyticsQueryService;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.AnalyticsSnapshot;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.queries.GetChurnAnalysisQuery;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.queries.GetExecutiveAnalyticsQuery;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.valueobjects.SnapshotPeriod;
import com.satecho.agrosafe.platform.apiservice.bi.domain.repositories.AnalyticsSnapshotRepository;
import com.satecho.agrosafe.platform.apiservice.iam.infrastructure.persistence.jpa.repositories.UserPersistenceRepository;
import com.satecho.agrosafe.platform.apiservice.iot.infrastructure.persistence.jpa.repositories.DevicePersistenceRepository;
import com.satecho.agrosafe.platform.apiservice.subscriptions.domain.model.valueobjects.SubscriptionStatus;
import com.satecho.agrosafe.platform.apiservice.subscriptions.infrastructure.persistence.jpa.assemblers.SubscriptionPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.subscriptions.infrastructure.persistence.jpa.repositories.SubscriptionPersistenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

@Service
@Transactional(readOnly = true)
public class AnalyticsQueryServiceImpl implements AnalyticsQueryService {

    private final AnalyticsSnapshotRepository analyticsSnapshotRepository;
    private final UserPersistenceRepository userRepository;
    private final DevicePersistenceRepository deviceRepository;
    private final SubscriptionPersistenceRepository subscriptionRepository;
    private final ObjectMapper objectMapper;

    public AnalyticsQueryServiceImpl(AnalyticsSnapshotRepository analyticsSnapshotRepository,
                                     UserPersistenceRepository userRepository,
                                     DevicePersistenceRepository deviceRepository,
                                     SubscriptionPersistenceRepository subscriptionRepository,
                                     ObjectMapper objectMapper) {
        this.analyticsSnapshotRepository = analyticsSnapshotRepository;
        this.userRepository = userRepository;
        this.deviceRepository = deviceRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public AnalyticsSnapshot getExecutiveAnalytics(GetExecutiveAnalyticsQuery query) {
        SnapshotPeriod period = resolvePeriod(query.period());
        AnalyticsSnapshot existing = analyticsSnapshotRepository
                .findTopByPeriodOrderByToDateDesc(period)
                .orElse(null);

        if (existing != null) {
            return existing;
        }

        return computeSnapshot(period);
    }

    @Override
    public AnalyticsSnapshot getChurnAnalysis(GetChurnAnalysisQuery query) {
        AnalyticsSnapshot latest = analyticsSnapshotRepository
                .findTopByPeriodOrderByToDateDesc(SnapshotPeriod.MONTHLY)
                .orElseGet(() -> computeSnapshot(SnapshotPeriod.MONTHLY));

        if (query.segment() != null && !query.segment().isBlank()) {
            ObjectNode segmented = objectMapper.createObjectNode();
            segmented.put("segment", query.segment());
            segmented.put("overallChurn", latest.getChurnRate());
            segmented.put("period", latest.getPeriod().name());
            segmented.put("fromDate", latest.getFromDate().toString());
            segmented.put("toDate", latest.getToDate().toString());

            String rawData;
            try {
                rawData = objectMapper.writeValueAsString(segmented);
            } catch (Exception e) {
                rawData = "{}";
            }

            return new AnalyticsSnapshot(
                    latest.getPeriod(), latest.getFromDate(), latest.getToDate(),
                    latest.getActiveUsers(), latest.getMrr(), latest.getCurrency(),
                    latest.getConversionRate(), latest.getChurnRate(),
                    latest.getNewSubscriptions(), latest.getCanceledSubscriptions(),
                    rawData
            );
        }

        return latest;
    }

    private SnapshotPeriod resolvePeriod(String period) {
        if (period == null || period.isBlank()) {
            return SnapshotPeriod.MONTHLY;
        }
        try {
            return SnapshotPeriod.valueOf(period.toUpperCase());
        } catch (IllegalArgumentException e) {
            return SnapshotPeriod.MONTHLY;
        }
    }

    private AnalyticsSnapshot computeSnapshot(SnapshotPeriod period) {
        long totalUsers = userRepository.count();
        long totalDevices = deviceRepository.count();
        long activeSubscriptions = subscriptionRepository.findAll().stream()
                .map(SubscriptionPersistenceAssembler::toDomainFromPersistence)
                .filter(s -> s.getStatus() == SubscriptionStatus.ACTIVE || s.getStatus() == SubscriptionStatus.TRIAL)
                .count();
        long canceledSubscriptions = subscriptionRepository.findAll().stream()
                .map(SubscriptionPersistenceAssembler::toDomainFromPersistence)
                .filter(s -> s.getStatus() == SubscriptionStatus.CANCELED)
                .count();
        long totalSubscriptions = activeSubscriptions + canceledSubscriptions;

        LocalDate now = LocalDate.now();
        Instant fromDate = computeFromDate(now, period);
        Instant toDate = now.atStartOfDay(ZoneId.systemDefault()).toInstant();

        int activeUsers = (int) activeSubscriptions;
        double mrr = computeMRR();
        double conversionRate = activeUsers > 0 ? (double) activeSubscriptions / activeUsers : 0.0;
        double churnRate = totalSubscriptions > 0
                ? (double) canceledSubscriptions / totalSubscriptions : 0.0;

        ObjectNode rawNode = objectMapper.createObjectNode();
        rawNode.put("totalUsers", totalUsers);
        rawNode.put("totalDevices", totalDevices);
        rawNode.put("activeSubscriptions", activeSubscriptions);
        rawNode.put("canceledSubscriptions", canceledSubscriptions);

        String rawData;
        try {
            rawData = objectMapper.writeValueAsString(rawNode);
        } catch (Exception e) {
            rawData = "{}";
        }

        return new AnalyticsSnapshot(period, fromDate, toDate, activeUsers, mrr,
                "USD", conversionRate, churnRate, (int) activeSubscriptions,
                (int) canceledSubscriptions, rawData);
    }

    private double computeMRR() {
        return subscriptionRepository.findAll().stream()
                .map(SubscriptionPersistenceAssembler::toDomainFromPersistence)
                .filter(s -> s.getStatus() == SubscriptionStatus.ACTIVE || s.getStatus() == SubscriptionStatus.TRIAL)
                .mapToDouble(s -> s.getPlanType().getPricePerHaPerMonth())
                .sum();
    }

    private Instant computeFromDate(LocalDate now, SnapshotPeriod period) {
        LocalDate from;
        switch (period) {
            case DAILY -> from = now.minusDays(1);
            case WEEKLY -> from = now.minusWeeks(1);
            case MONTHLY -> from = now.minusMonths(1);
            case QUARTERLY -> from = now.minusMonths(3);
            default -> from = now.minusMonths(1);
        }
        return from.atStartOfDay(ZoneId.systemDefault()).toInstant();
    }
}
