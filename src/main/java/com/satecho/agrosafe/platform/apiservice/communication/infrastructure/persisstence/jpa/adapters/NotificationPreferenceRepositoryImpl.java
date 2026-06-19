package com.satecho.agrosafe.platform.apiservice.communication.infrastructure.persisstence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.communication.domain.model.aggregates.NotificationPreference;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.NotificationType;
import com.satecho.agrosafe.platform.apiservice.communication.domain.repositories.NotificationPreferenceRepository;
import com.satecho.agrosafe.platform.apiservice.communication.infrastructure.persisstence.jpa.assemblers.NotificationPreferencePersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.communication.infrastructure.persisstence.jpa.repossitories.NotificationPreferencePersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class NotificationPreferenceRepositoryImpl implements NotificationPreferenceRepository {

    private final NotificationPreferencePersistenceRepository persistenceRepository;

    public NotificationPreferenceRepositoryImpl(NotificationPreferencePersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public NotificationPreference save(NotificationPreference preference) {
        var saved = persistenceRepository.save(NotificationPreferencePersistenceAssembler.toPersistenceFromDomain(preference));
        return NotificationPreferencePersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public List<NotificationPreference> findByUserId(Long userId) {
        return persistenceRepository.findByUserId(userId).stream()
                .map(NotificationPreferencePersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public Optional<NotificationPreference> findByUserIdAndNotificationType(Long userId, NotificationType notificationType) {
        return persistenceRepository.findByUserIdAndNotificationType(userId, notificationType)
                .map(NotificationPreferencePersistenceAssembler::toDomainFromPersistence);
    }
}