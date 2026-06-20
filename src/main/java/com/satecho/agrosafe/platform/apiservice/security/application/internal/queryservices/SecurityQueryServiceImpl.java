package com.satecho.agrosafe.platform.apiservice.security.application.internal.queryservices;

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

    public SecurityQueryServiceImpl(SecurityEventRepository securityEventRepository) {
        this.securityEventRepository = securityEventRepository;
    }

    @Override
    public List<SecurityEvent> handle(GetSecurityEventsByFarmQuery query) {
        return securityEventRepository.findByFarmIdWithFilters(query.farmId(), query.from(), query.to(),
                query.severity(), query.classification(), query.limit(), query.page());
    }

    @Override
    public Optional<SecurityEvent> handle(GetSecurityEventByIdQuery query) {
        return securityEventRepository.findById(query.eventId());
    }
}
