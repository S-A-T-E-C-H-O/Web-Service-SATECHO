package com.satecho.agrosafe.platform.apiservice.agronomist.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.agronomist.application.commandservices.FieldVisitCommandService;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.aggregates.FieldVisit;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.model.commands.ScheduleFieldVisitCommand;
import com.satecho.agrosafe.platform.apiservice.agronomist.domain.repositories.FieldVisitRepository;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FieldVisitCommandServiceImpl implements FieldVisitCommandService {

    private final FieldVisitRepository fieldVisitRepository;

    public FieldVisitCommandServiceImpl(FieldVisitRepository fieldVisitRepository) {
        this.fieldVisitRepository = fieldVisitRepository;
    }

    @Override
    public Result<FieldVisit, ApplicationError> scheduleVisit(ScheduleFieldVisitCommand command) {
        var visit = new FieldVisit(command.agronomistUserId(), command.farmId(), command.scheduledAt(),
                command.tag(), command.noteTitle(), command.noteBody(), command.urgent());
        return Result.success(fieldVisitRepository.save(visit));
    }

    @Override
    public Result<FieldVisit, ApplicationError> completeVisit(Long visitId, Long currentUserId) {
        var visit = fieldVisitRepository.findById(visitId);
        if (visit.isEmpty()) {
            return Result.failure(ApplicationError.notFound("FieldVisit", String.valueOf(visitId)));
        }
        var v = visit.get();
        if (!v.getAgronomistUserId().equals(currentUserId)) {
            return Result.failure(ApplicationError.forbidden("FieldVisit", "Only the assigned agronomist can complete this visit"));
        }
        v.complete();
        return Result.success(fieldVisitRepository.save(v));
    }
}
