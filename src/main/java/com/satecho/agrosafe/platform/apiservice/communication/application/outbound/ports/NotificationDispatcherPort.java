package com.satecho.agrosafe.platform.apiservice.communication.application.outbound.ports;

import com.satecho.agrosafe.platform.apiservice.communication.domain.model.aggregates.Notification;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;

public interface NotificationDispatcherPort {
    Result<Void, ApplicationError> dispatch(Notification notification);
}