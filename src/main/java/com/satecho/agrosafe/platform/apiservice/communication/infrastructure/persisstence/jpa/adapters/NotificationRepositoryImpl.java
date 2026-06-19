package com.satecho.agrosafe.platform.apiservice.communication.infrastructure.persisstence.jpa.adapters;

import com.satecho.agrosafe.platform.apiservice.communication.domain.model.aggregates.Notification;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.DeliveryStatus;
import com.satecho.agrosafe.platform.apiservice.communication.domain.model.valueobjects.NotificationType;
import com.satecho.agrosafe.platform.apiservice.communication.domain.repositories.NotificationRepository;
import com.satecho.agrosafe.platform.apiservice.communication.infrastructure.persisstence.jpa.assemblers.NotificationPersistenceAssembler;
import com.satecho.agrosafe.platform.apiservice.communication.infrastructure.persisstence.jpa.repossitories.NotificationPersistenceRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationPersistenceRepository persistenceRepository;

    public NotificationRepositoryImpl(NotificationPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public Notification save(Notification notification) {
        var saved = persistenceRepository.save(NotificationPersistenceAssembler.toPersistenceFromDomain(notification));
        return NotificationPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return persistenceRepository.findById(id).map(NotificationPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<Notification> findByUserIdWithFilters(Long userId, DeliveryStatus status, NotificationType type, Boolean read, int limit) {
        return persistenceRepository.findByUserIdWithFilters(userId, status, type, read, PageRequest.of(0, limit))
                .stream().map(NotificationPersistenceAssembler::toDomainFromPersistence).toList();
    }
}