package com.satecho.agrosafe.platform.apiservice.soil.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.soil.domain.model.aggregates.Diagnosis;
import com.satecho.agrosafe.platform.apiservice.soil.domain.model.commands.GenerateDiagnosisCommand;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;

public interface DiagnosisCommandService {
    Result<Diagnosis, ApplicationError> generateDiagnosis(GenerateDiagnosisCommand command);
}
