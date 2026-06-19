package com.satecho.agrosafe.platform.apiservice.communication.infrastructure.outbound.email;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.aggregates.Notification;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ResendEmailAdapter {

    private static final Logger log = LoggerFactory.getLogger(ResendEmailAdapter.class);

    private final Resend resend;
    private final String from;

    public ResendEmailAdapter(@Value("${resend.api-key}") String apiKey,
                              @Value("${resend.from}") String from) {
        this.resend = new Resend(apiKey);
        this.from = from;
    }

    public Result<Void, ApplicationError> send(Notification notification) {
        if (notification.getRecipientAddress() == null) {
            return Result.failure(ApplicationError.validationError("recipientAddress", "Email address required for EMAIL channel"));
        }
        try {
            var params = CreateEmailOptions.builder()
                    .from(from)
                    .to(List.of(notification.getRecipientAddress()))
                    .subject(notification.getTitle())
                    .html("<p>" + notification.getBody() + "</p>")
                    .build();
            resend.emails().send(params);
            return Result.success(null);
        } catch (ResendException e) {
            log.error("Resend email failed for userId={}: {}", notification.getUserId(), e.getMessage());
            return Result.failure(ApplicationError.unexpected("ResendEmail", e.getMessage()));
        }
    }
}