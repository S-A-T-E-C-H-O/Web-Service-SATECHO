package com.satecho.agrosafe.platform.apiservice.agronomist.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.agronomist.application.commandservices.FieldVisitCommandService;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.FieldVisit;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.commands.CompleteFieldVisitCommand;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.commands.ScheduleFieldVisitCommand;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.repositories.FieldVisitRepository;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the {@link FieldVisitCommandService} interface.
 * Handles commands related to scheduling and completing field visits.
 */
@Service
@Transactional
public class FieldVisitCommandServiceImpl implements FieldVisitCommandService {

    /**
     * Repository for accessing field visit data.
     */
    private final FieldVisitRepository fieldVisitRepository;

    /**
     * Constructs a {@code FieldVisitCommandServiceImpl} with the specified repository.
     *
     * @param fieldVisitRepository the repository for managing field visits
     */
    public FieldVisitCommandServiceImpl(FieldVisitRepository fieldVisitRepository) {
        this.fieldVisitRepository = fieldVisitRepository;
    }

    /**
     * Schedules a new field visit.
     *
     * @param command the command containing scheduling details
     * @return a Result containing the scheduled FieldVisit if successful, or an ApplicationError if not
     */
    @Override
    public Result<FieldVisit, ApplicationError> scheduleVisit(ScheduleFieldVisitCommand command) {
        var visit = new FieldVisit(command.agronomistUserId(), command.farmId(), command.scheduledAt(),
                command.tag(), command.noteTitle(), command.noteBody(), command.urgent());
        if (command.latitude() != null || command.longitude() != null) {
            visit.recordLocation(command.latitude(), command.longitude());
        }
        if (command.photoBase64() != null) {
            visit.attachPhoto(command.photoBase64());
        }
        return Result.success(fieldVisitRepository.save(visit));
    }

    /**
     * Completes an existing field visit.
     *
     * @param command the command containing completion details
     * @return a Result containing the completed FieldVisit if successful, or an ApplicationError if not
     */
    @Override
    public Result<FieldVisit, ApplicationError> completeVisit(CompleteFieldVisitCommand command) {
        var visit = fieldVisitRepository.findById(command.visitId());
        if (visit.isEmpty()) {
            return Result.failure(ApplicationError.notFound("FieldVisit", String.valueOf(command.visitId())));
        }
        var v = visit.get();
        if (!v.getAgronomistUserId().equals(command.currentUserId())) {
            return Result.failure(ApplicationError.forbidden("FieldVisit", "Only the assigned agronomist can complete this visit"));
        }
        v.complete();
        if (command.latitude() != null || command.longitude() != null) {
            v.recordLocation(command.latitude(), command.longitude());
        }
        if (command.photoBase64() != null) {
            v.attachPhoto(command.photoBase64());
        }
        return Result.success(fieldVisitRepository.save(v));
    }
}
