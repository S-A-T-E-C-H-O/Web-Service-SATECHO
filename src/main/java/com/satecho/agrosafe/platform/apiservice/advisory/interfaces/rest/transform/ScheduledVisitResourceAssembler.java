package com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.transform;

import com.satecho.agrosafe.platform.apiservice.advisory.domain.model.aggregates.FieldVisit;
import com.satecho.agrosafe.platform.apiservice.advisory.interfaces.rest.resources.ScheduledVisitResource;
import com.satecho.agrosafe.platform.apiservice.iam.domain.model.aggregates.User;
import com.satecho.agrosafe.platform.apiservice.onboarding.domain.model.aggregates.Farm;

public final class ScheduledVisitResourceAssembler {
    private ScheduledVisitResourceAssembler() {}

    public static ScheduledVisitResource toResource(FieldVisit visit, Farm farm, User owner) {
        return new ScheduledVisitResource(
                visit.getId(), visit.getAgronomistId(), visit.getFarmId(),
                farm != null ? farm.getName() : null,
                owner != null ? owner.getFullName() : null,
                visit.getScheduledAt(), visit.getTag(),
                visit.getNoteTitle(), visit.getNoteBody(),
                visit.getUrgent(), visit.getStatus().name());
    }
}
