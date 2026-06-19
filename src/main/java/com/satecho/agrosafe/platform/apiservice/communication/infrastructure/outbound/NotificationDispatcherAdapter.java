package com.satecho.agrosafe.platform.apiservice.communication.infrastructure.outbound;

import com.satecho.agrosafe.platform.apiservice.communication.application.outbound.ports.NotificationDispatcherPort;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.aggregates.Notification;
import com.satecho.agrosafe.platform.apiservice.communication.infrastructure.outbound.email.ResendEmailAdapter;
import com.satecho.agrosafe.platform.apiservice.communication.infrastructure.outbound.firebase.FirebasePushAdapter;
import com.satecho.agrosafe.platform.apiservice.communication.infrastructure.outbound.twilio.TwilioSmsAdapter;
import com.satecho.agrosafe.platform.apiservice.communication.infrastructure.outbound.twilio.TwilioWhatsAppAdapter;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import org.springframework.stereotype.Component;

@Component
public class NotificationDispatcherAdapter implements NotificationDispatcherPort {

    private final FirebasePushAdapter firebasePushAdapter;
    private final TwilioWhatsAppAdapter twilioWhatsAppAdapter;
    private final TwilioSmsAdapter twilioSmsAdapter;
    private final ResendEmailAdapter resendEmailAdapter;

    public NotificationDispatcherAdapter(FirebasePushAdapter firebasePushAdapter,
                                         TwilioWhatsAppAdapter twilioWhatsAppAdapter,
                                         TwilioSmsAdapter twilioSmsAdapter,
                                         ResendEmailAdapter resendEmailAdapter) {
        this.firebasePushAdapter = firebasePushAdapter;
        this.twilioWhatsAppAdapter = twilioWhatsAppAdapter;
        this.twilioSmsAdapter = twilioSmsAdapter;
        this.resendEmailAdapter = resendEmailAdapter;
    }

    @Override
    public Result<Void, ApplicationError> dispatch(Notification notification) {
        return switch (notification.getChannel()) {
            case PUSH -> firebasePushAdapter.send(notification);
            case WHATSAPP -> twilioWhatsAppAdapter.send(notification);
            case SMS -> twilioSmsAdapter.send(notification);
            case EMAIL -> resendEmailAdapter.send(notification);
        };
    }
}