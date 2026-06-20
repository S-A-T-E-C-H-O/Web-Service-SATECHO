package com.satecho.agrosafe.platform.apiservice.security.application.queryservices;

import com.satecho.agrosafe.platform.apiservice.security.domain.model.aggregates.SecurityEvent;
import com.satecho.agrosafe.platform.apiservice.security.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface SecurityQueryService {
    List<SecurityEvent> handle(GetSecurityEventsByFarmQuery query);
    Optional<SecurityEvent> handle(GetSecurityEventByIdQuery query);
}
