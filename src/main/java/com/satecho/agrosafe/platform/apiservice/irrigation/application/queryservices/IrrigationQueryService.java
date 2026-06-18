package com.satecho.agrosafe.platform.apiservice.irrigation.application.queryservices;

import com.satecho.agrosafe.platform.irrigation.domain.model.aggregates.IrrigationSession;
import com.satecho.agrosafe.platform.irrigation.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface IrrigationQueryService {
    Optional<IrrigationSession> handle(GetActiveSessionByZoneQuery query);
    List<IrrigationSession> handle(GetSessionHistoryByZoneQuery query);
}
