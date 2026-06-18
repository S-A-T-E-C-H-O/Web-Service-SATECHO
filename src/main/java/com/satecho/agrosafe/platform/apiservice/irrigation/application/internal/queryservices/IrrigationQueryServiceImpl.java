package com.satecho.agrosafe.platform.apiservice.irrigation.application.internal.queryservices;

import com.satecho.agrosafe.platform.irrigation.application.queryservices.IrrigationQueryService;
import com.satecho.agrosafe.platform.irrigation.domain.model.aggregates.IrrigationSession;
import com.satecho.agrosafe.platform.irrigation.domain.model.queries.*;
import com.satecho.agrosafe.platform.irrigation.domain.model.valueobjects.IrrigationStatus;
import com.satecho.agrosafe.platform.irrigation.domain.repositories.IrrigationSessionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class IrrigationQueryServiceImpl implements IrrigationQueryService {

    private final IrrigationSessionRepository sessionRepository;

    public IrrigationQueryServiceImpl(IrrigationSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Override
    public Optional<IrrigationSession> handle(GetActiveSessionByZoneQuery query) {
        return sessionRepository.findByZoneIdAndStatus(query.zoneId(), IrrigationStatus.ACTIVE);
    }

    @Override
    public List<IrrigationSession> handle(GetSessionHistoryByZoneQuery query) {
        if (query.fromDate() != null && query.toDate() != null)
            return sessionRepository.findByZoneIdAndStartedAtBetween(query.zoneId(), query.fromDate(), query.toDate(), query.limit());
        return sessionRepository.findByZoneIdOrderByStartedAtDesc(query.zoneId(), query.limit());
    }
}
