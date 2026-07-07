package com.satecho.agrosafe.platform.apiservice.security.application.internal.queryservices;

import com.satecho.agrosafe.platform.apiservice.edge.application.services.DemoSharedDeviceLinkService;
import com.satecho.agrosafe.platform.apiservice.security.application.queryservices.SecurityQueryService;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.aggregates.SecurityEvent;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.queries.*;
import com.satecho.agrosafe.platform.apiservice.security.domain.repositories.SecurityEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SecurityQueryServiceImpl implements SecurityQueryService {

    private final SecurityEventRepository securityEventRepository;
    private final DemoSharedDeviceLinkService demoSharedDeviceLinkService;

    public SecurityQueryServiceImpl(SecurityEventRepository securityEventRepository,
                                    DemoSharedDeviceLinkService demoSharedDeviceLinkService) {
        this.securityEventRepository = securityEventRepository;
        this.demoSharedDeviceLinkService = demoSharedDeviceLinkService;
    }

    @Override
    public List<SecurityEvent> handle(GetSecurityEventsByFarmQuery query) {
        var realEvents = securityEventRepository.findByFarmIdWithFilters(query.farmId(), query.from(), query.to(),
                query.severity(), query.classification(), query.limit(), query.page());
        var demoEvents = demoSharedDeviceLinkService.findActiveByFarmId(query.farmId())
                .map(link -> securityEventRepository.findByDeviceIdWithFilters(
                                link.getPhysicalDeviceId(), query.from(), query.to(),
                                query.severity(), query.classification(), query.limit(), query.page()).stream()
                        .map(event -> mapDemoEvent(event, link.getFarmId(), link.getZoneId()))
                        .toList())
                .orElse(List.of());
        if (demoEvents.isEmpty()) return realEvents;
        var merged = new java.util.ArrayList<SecurityEvent>(realEvents);
        merged.addAll(demoEvents);
        merged.sort((a, b) -> b.getDetectedAt().compareTo(a.getDetectedAt()));
        return merged.stream().limit(query.limit()).toList();
    }

    @Override
    public Optional<SecurityEvent> handle(GetSecurityEventByIdQuery query) {
        return securityEventRepository.findById(query.eventId());
    }

    private SecurityEvent mapDemoEvent(SecurityEvent source, Long farmId, Long zoneId) {
        var mapped = new SecurityEvent();
        mapped.setId(source.getId());
        mapped.setFarmId(farmId);
        mapped.setDeviceId(source.getDeviceId());
        mapped.setZoneId(zoneId);
        mapped.setClassification(source.getClassification());
        mapped.setConfidenceLevel(source.getConfidenceLevel());
        mapped.setSeverity(source.getSeverity());
        mapped.setDetectedAt(source.getDetectedAt());
        mapped.setAcknowledged(source.getAcknowledged());
        mapped.setAcknowledgedBy(source.getAcknowledgedBy());
        mapped.setAcknowledgedAt(source.getAcknowledgedAt());
        mapped.setLocationDescription(source.getLocationDescription());
        mapped.setRawData(source.getRawData());
        mapped.setPulseDurationMs(source.getPulseDurationMs());
        mapped.setTriggersPerMinute(source.getTriggersPerMinute());
        return mapped;
    }
}
