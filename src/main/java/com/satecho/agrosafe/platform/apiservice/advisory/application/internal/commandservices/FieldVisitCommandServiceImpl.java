package com.satecho.agrosafe.platform.apiservice.advisory.application.internal.commandservices;

import com.satecho.agrosafe.platform.apiservice.advisory.application.commandservices.FieldVisitCommandService;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.FieldVisit;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.commands.CompleteVisitCommand;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.commands.ScheduleVisitCommand;
import com.satecho.agrosafe.platform.apiservice.advisory.domain.repositories.FieldVisitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FieldVisitCommandServiceImpl implements FieldVisitCommandService {

    private final FieldVisitRepository repository;

    public FieldVisitCommandServiceImpl(FieldVisitRepository repository) {
        this.repository = repository;
    }

    @Override
    public FieldVisit schedule(ScheduleVisitCommand command) {
        var visit = new FieldVisit(command.agronomistId(), command.farmId(),
                command.scheduledAt(), command.tag(), command.noteTitle(),
                command.noteBody(), command.urgent());
        return repository.save(visit);
    }

    @Override
    public FieldVisit complete(CompleteVisitCommand command) {
        var visit = repository.findById(command.visitId())
                .orElseThrow(() -> new IllegalArgumentException("Visit not found: " + command.visitId()));
        visit.complete();
        return repository.save(visit);
    }
}
