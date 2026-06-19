package com.satecho.agrosafe.platform.apiservice.communication.infrastructure.outbound.twilio;

import com.satecho.agrosafe.platform.apiservice.communication.domain.model.aggregates.Notification;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TwilioSmsAdapter {

    private static final Logger log = LoggerFactory.getLogger(TwilioSmsAdapter.class);

    @Value("${twilio.enabled:false}")
    private boolean twilioEnabled;

    @Value("${twilio.sms-from:}")
    private String from;

    public Result<Void, ApplicationError> send(Notification notification) {
        if (!twilioEnabled) {
            return Result.failure(ApplicationError.unexpected("TwilioSms", "Twilio not configured (TWILIO_ENABLED=false)"));
        }
        if (notification.getRecipientAddress() == null) {
            return Result.failure(ApplicationError.validationError("recipientAddress", "Phone number required for SMS channel"));
        }
        try {
            Message.creator(new PhoneNumber(notification.getRecipientAddress()), new PhoneNumber(from),
                    notification.getTitle() + "\n\n" + notification.getBody()).create();
            return Result.success(null);
        } catch (Exception e) {
            log.error("SMS send failed for userId={}: {}", notification.getUserId(), e.getMessage());
            return Result.failure(ApplicationError.unexpected("TwilioSms", e.getMessage()));
        }
    }
}