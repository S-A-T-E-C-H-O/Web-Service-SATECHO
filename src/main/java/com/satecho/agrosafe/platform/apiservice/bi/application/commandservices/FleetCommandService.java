package com.satecho.agrosafe.platform.apiservice.bi.application.commandservices;

import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.DiagnosticSession;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.aggregates.FirmwareRelease;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.commands.BatchRegisterDevicesCommand;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.commands.CreateFirmwareCommand;
import com.satecho.agrosafe.platform.apiservice.bi.domain.model.commands.StartDiagnosticCommand;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.ApplicationError;
import com.satecho.agrosafe.platform.apiservice.shared.application.result.Result;

import java.util.List;

public interface FleetCommandService {
    Result<List<String>, ApplicationError> batchRegisterDevices(BatchRegisterDevicesCommand command);
    Result<DiagnosticSession, ApplicationError> startDiagnostic(StartDiagnosticCommand command);
    Result<FirmwareRelease, ApplicationError> createFirmware(CreateFirmwareCommand command);
}
