package com.satecho.agrosafe.platform.apiservice.communication.domain.model.aggregates;

import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.NotificationChannel;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.NotificationType;
import com.satecho.agrosafe.platform.apiservice.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class NotificationPreference extends AuditableAbstractAggregateRoot<NotificationPreference> {
    @Setter private Long id;
    @Setter private Long userId;
    @Setter private NotificationType notificationType;
    @Setter private String channelsEnabled;
    @Setter private Boolean dailyDigestEnabled;
    @Setter private String quietHoursStart;
    @Setter private String quietHoursEnd;

    public NotificationPreference() {}

    public NotificationPreference(Long userId, NotificationType notificationType,
                                  List<NotificationChannel> channelsEnabled, Boolean dailyDigestEnabled,
                                  String quietHoursStart, String quietHoursEnd) {
        this.userId = userId;
        this.notificationType = notificationType;
        this.channelsEnabled = serializeChannels(channelsEnabled);
        this.dailyDigestEnabled = dailyDigestEnabled != null ? dailyDigestEnabled : false;
        this.quietHoursStart = quietHoursStart;
        this.quietHoursEnd = quietHoursEnd;
    }

    public static NotificationPreference createDefault(Long userId, NotificationType notificationType) {
        List<NotificationChannel> defaultChannels = switch (notificationType) {
            case SECURITY_ALERT -> List.of(NotificationChannel.PUSH, NotificationChannel.WHATSAPP);
            case IRRIGATION_ALERT, EC_ALERT -> List.of(NotificationChannel.PUSH);
            case LOW_BATTERY, MAINTENANCE -> List.of(NotificationChannel.PUSH, NotificationChannel.EMAIL);
            case SYSTEM, DAILY_SUMMARY -> List.of(NotificationChannel.EMAIL);
        };
        return new NotificationPreference(userId, notificationType, defaultChannels, true, "22:00", "07:00");
    }

    public void update(List<NotificationChannel> channelsEnabled, Boolean dailyDigestEnabled,
                       String quietHoursStart, String quietHoursEnd) {
        this.channelsEnabled = serializeChannels(channelsEnabled);
        this.dailyDigestEnabled = dailyDigestEnabled != null ? dailyDigestEnabled : this.dailyDigestEnabled;
        this.quietHoursStart = quietHoursStart;
        this.quietHoursEnd = quietHoursEnd;
    }

    public List<NotificationChannel> getChannelsEnabledList() {
        if (channelsEnabled == null || channelsEnabled.isBlank() || "[]".equals(channelsEnabled)) return List.of();
        List<NotificationChannel> result = new ArrayList<>();
        String content = channelsEnabled.replace("[", "").replace("]", "").replace("\"", "");
        if (!content.isBlank()) {
            for (String part : content.split(",")) {
                String trimmed = part.trim();
                if (!trimmed.isBlank()) {
                    try { result.add(NotificationChannel.valueOf(trimmed)); } catch (IllegalArgumentException ignored) {}
                }
            }
        }
        return result;
    }

    private String serializeChannels(List<NotificationChannel> channels) {
        if (channels == null || channels.isEmpty()) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < channels.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append("\"").append(channels.get(i).name()).append("\"");
        }
        return sb.append("]").toString();
    }
}